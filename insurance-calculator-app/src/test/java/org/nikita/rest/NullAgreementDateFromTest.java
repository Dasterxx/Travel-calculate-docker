package org.nikita.rest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
class NullAgreementDateFromTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void compareJsonResponseNullAgreementDateFrom() throws Exception {
        executeAndCompare(
                "rest/request/nullAgreementDateFrom.json",
                "rest/response/nullAgreementDateFrom.json"
        );
    }
}
