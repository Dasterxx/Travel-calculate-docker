package org.nikita.rest;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("test")
class InvalidCountryEntityRequestsTest extends TravelCalculatePremiumControllerTest {

    @Test
    public void shouldReturnErrorWhenCountryIsMissing() throws Exception {
        executeAndCompare(
                "rest/request/emptyCountry.json",
                "rest/response/CountryMissing.json"
        );
    }

    @Test
    public void shouldReturnErrorWhenCountryIsNull() throws Exception {
        executeAndCompare(
                "rest/request/nullCountry.json",
                "rest/response/CountryIsNull.json"
        );
    }

    @Test
    public void shouldReturnErrorWhenCountryIsEmpty() throws Exception {
        executeAndCompare(
                "rest/request/emptyCountry.json",
                "rest/response/CountryIsEmpty.json"
        );
    }

    @Test
    public void shouldPassWhenCountryIsValid() throws Exception {
        executeAndCompare(
                "rest/request/validCountry.json",
                "rest/response/validCountry.json"
        );
    }
}
