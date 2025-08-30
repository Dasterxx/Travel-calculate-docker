package org.nikita.rest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Disabled
public class InvalidPersonFirstNameTest extends TravelCalculatePremiumControllerTest{
    @Test
    public void compareJsonResponseMissingPersonFirstName() throws Exception {
        executeAndCompare(
                "rest/request/invalidPersonFirstName.json",
                "rest/response/invalidPersonFirstName.json"
        );
    }
}
