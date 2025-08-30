package org.nikita.rest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
class ValidMedicalRiskTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void compareJsonResponseInValidMedicalRiskLevelRequest() throws Exception {
        executeAndCompare(
                "rest/request/InvalidMedicalRiskLevel.json",
                "rest/response/InvalidMedicalRiskLevel.json"
        );
    }
}
