package org.nikita.rest.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
class TravelCalculatePremiumResponseLogger {

    private static final Logger logger = LoggerFactory.getLogger(TravelCalculatePremiumResponseLogger.class);
    private static final Logger appLogger = LoggerFactory.getLogger("org.javaguru.travel.insurance.app");
    private final ObjectMapper objectMapper;

    TravelCalculatePremiumResponseLogger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    void log(TravelCalculatePremiumCoreResult response) {
        Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            String json = objectMapper.writeValueAsString(response);
            logger.info("RESPONSE: {}", json);
        } catch (JsonProcessingException e) {
            logger.error("Error to convert request to JSON", e);
        }finally {
            logger.info("RESPONSE TIME: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        }
        appLogger.info("Some general application log message");
    }

}
