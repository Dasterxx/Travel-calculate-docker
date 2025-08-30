package org.nikita.core.validations.agreement.country;

import org.junit.jupiter.api.Test;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountryEntityValidationTest {

    private final CountryValidation countryValidation = new CountryValidation();

    @Test
    public void shouldReturnErrorWhenCountriesToVisitIsNull() {
        TravelCalculatePremiumCoreCommand result = buildResultWithCountries(null);

        Optional<ValidationErrorDto> validationResult = countryValidation.validate(result);

        assertTrue(validationResult.isPresent());
        assertEquals("countriesToVisit", validationResult.get().errorCode());
        assertEquals("ERROR_CODE_23", validationResult.get().description());
    }

    @Test
    public void shouldReturnErrorWhenCountriesToVisitIsEmpty() {
        TravelCalculatePremiumCoreCommand result = buildResultWithCountries(new ArrayList<>());

        Optional<ValidationErrorDto> validationResult = countryValidation.validate(result);

        assertTrue(validationResult.isPresent());
        assertEquals("countriesToVisit", validationResult.get().errorCode());
        assertEquals("ERROR_CODE_23", validationResult.get().description());
    }

    @Test
    public void shouldReturnErrorWhenCountriesToVisitContainsEmptyString() {
        List<String> countries = new ArrayList<>();
        countries.add("");
        TravelCalculatePremiumCoreCommand result = buildResultWithCountries(countries);

        Optional<ValidationErrorDto> validationResult = countryValidation.validate(result);

        assertTrue(validationResult.isPresent());
        assertEquals("countriesToVisit", validationResult.get().errorCode());
        assertEquals("ERROR_CODE_23", validationResult.get().description());
    }

    @Test
    public void shouldNotReturnErrorWhenCountriesToVisitIsValid() {
        List<String> countries = new ArrayList<>();
        countries.add("SPAIN");
        TravelCalculatePremiumCoreCommand result = buildResultWithCountries(countries);

        Optional<ValidationErrorDto> validationResult = countryValidation.validate(result);

        assertTrue(validationResult.isEmpty());
    }

    private TravelCalculatePremiumCoreCommand buildResultWithCountries(List<String> countries) {
        AgreementDto agreementResponse = new AgreementDto();
        agreementResponse.setCountriesToVisit(countries);
        TravelCalculatePremiumCoreCommand result = new TravelCalculatePremiumCoreCommand();
        result.setAgreement(agreementResponse);
        return result;
    }
}
