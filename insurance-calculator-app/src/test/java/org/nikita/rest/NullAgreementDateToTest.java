package org.nikita.rest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
class NullAgreementDateToTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void compareJsonResponseNullAgreementDateTo() throws Exception {
        executeAndCompare(
                "rest/request/nullAgreementDateTo.json",
                "rest/response/nullAgreementDateTo.json"
        );
    }
}
