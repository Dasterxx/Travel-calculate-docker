package org.nikita.loadtesting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StopWatch;

import java.nio.charset.StandardCharsets;

public abstract class CommonCall implements Runnable {

    protected final RestTemplate restTemplate = new RestTemplate();

    protected abstract String getUrl();
    protected abstract String getRequestResource();
    protected abstract String getExpectedResponseResource();

    protected final LoadTestingStatistic statistic;

    public CommonCall(LoadTestingStatistic statistic) {
        this.statistic = statistic;
    }

    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch(getClass().getSimpleName());
        stopWatch.start();

        try {
            String requestJson = readFileAsString(getRequestResource());
            String expectedResponse = readFileAsString(getExpectedResponseResource());

            ResponseEntity<String> responseEntity = sendPost(getUrl(), requestJson);

            String actualResponse = responseEntity.getBody() != null ? responseEntity.getBody().trim() : "";
            int statusCode = responseEntity.getStatusCode().value();

            System.out.println("[" + getClass().getSimpleName() + "] Response status: " + statusCode);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expectedJson = objectMapper.readTree(expectedResponse);
            JsonNode actualJson = objectMapper.readTree(actualResponse);

            if (expectedJson.equals(actualJson)) {
                System.out.println("[" + getClass().getSimpleName() + "] SUCCESS: Response matches expected.");
            } else {
                System.err.println("[" + getClass().getSimpleName() + "] FAIL: Response does not match expected!");
                System.err.println("Expected: " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expectedJson));
                System.err.println("Actual:   " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actualJson));
            }
        } catch (Exception e) {
            System.err.println("[" + getClass().getSimpleName() + "] Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            long elapsed = stopWatch.getTotalTimeMillis();
            System.out.println("[" + getClass().getSimpleName() + "] Finished in " + elapsed + " ms");
            if (statistic != null) {
                statistic.addExecutionTime(elapsed);
            }
        }
    }


    private String readFileAsString(String resourcePath) throws Exception {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).trim();
        }
    }

    private ResponseEntity<String> sendPost(String url, String requestJson) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}
