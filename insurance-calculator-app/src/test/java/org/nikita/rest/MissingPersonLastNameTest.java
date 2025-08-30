package org.nikita.rest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Disabled
class MissingPersonLastNameTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void compareJsonResponseMissingPersonLastName() throws Exception {
        executeAndCompare(
                "rest/request/missingPersonLastName.json",
                "rest/response/missingPersonLastName.json"
        );
    }
}
