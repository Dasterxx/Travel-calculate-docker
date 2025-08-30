package org.nikita.rest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
class InvalidDateRangeTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void compareJsonResponseInvalidDateRange() throws Exception {
        executeAndCompare(
                "rest/request/invalidDateRange.json",
                "rest/response/invalidDateRange.json"
        );
    }
}
