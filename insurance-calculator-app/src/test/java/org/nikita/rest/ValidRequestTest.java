package org.nikita.rest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
class ValidRequestTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void compareJsonResponseValidRequest() throws Exception {
        executeAndCompare(
                "rest/request/validRequest.json",
                "rest/response/validResponse.json"
        );
    }
}
