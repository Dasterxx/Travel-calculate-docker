package org.nikita.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String extractTokenFromResponse(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode dataNode = rootNode.path("data");
            return dataNode.path("token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract token from response", e);
        }
    }
}

