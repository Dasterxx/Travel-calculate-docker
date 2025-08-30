package org.nikita.rest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Disabled
class MissingPersonFirstNameTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void compareJsonResponseMissingPersonFirstName() throws Exception {
        executeAndCompare(
                "rest/request/missingPersonFirstName.json",
                "rest/response/missingPersonFirstName.json"
        );
    }
}
