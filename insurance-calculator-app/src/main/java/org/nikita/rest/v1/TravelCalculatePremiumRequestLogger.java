package org.nikita.rest.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
class TravelCalculatePremiumRequestLogger {

    private static final Logger requestLogger = LoggerFactory.getLogger(TravelCalculatePremiumRequestLogger.class);
    private static final Logger appLogger = LoggerFactory.getLogger("org.javaguru.travel.insurance.app");
    private final ObjectMapper objectMapper;

    TravelCalculatePremiumRequestLogger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    void log(TravelCalculatePremiumCoreCommand request) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            String json = objectMapper.writeValueAsString(request);
            requestLogger.info("REQUEST: {}", json);
        } catch (JsonProcessingException e) {
            requestLogger.error("Error to convert request to JSON", e);
        } finally {
            stopwatch.stop();
            requestLogger.info("REQUEST LOGGING TIME: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
        }

    }
}
