package org.nikita.core.validations.agreement.country;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateCountryEntityForMedicalRiskTest {

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    @InjectMocks
    private ValidateCountryForMedicalRisk validateCountryForMedicalRisk;

    private TravelCalculatePremiumCoreCommand buildResult(List<String> selectedRisks, List<String> countriesToVisit) {
        AgreementDto agreementResponse = new AgreementDto();
        agreementResponse.setSelectedRisks(selectedRisks);
        agreementResponse.setCountriesToVisit(countriesToVisit);

        TravelCalculatePremiumCoreCommand result = new TravelCalculatePremiumCoreCommand();
        result.setAgreement(agreementResponse);
        return result;
    }

    private static final String ERROR_CODE = "ERROR_CODE_14";
    private static final String ERROR_MESSAGE = "Country must be specified for medical expenses risk.";

    @Test
    void shouldReturnErrorWhenMedicalExpensesSelectedAndCountryIsNull() {
        List<String> selectedRisks = List.of("Medical Expenses");
        List<String> countriesToVisit = null;

        when(errorCodeProperties.getErrorDescription(ERROR_CODE)).thenReturn(ERROR_MESSAGE);

        TravelCalculatePremiumCoreCommand request = buildResult(selectedRisks, countriesToVisit);

        Optional<ValidationErrorDto> error = validateCountryForMedicalRisk.validate(request);

        assertTrue(error.isPresent());
        assertEquals("country", error.get().errorCode());
        assertEquals(ERROR_MESSAGE, error.get().description());
    }

    @Test
    void shouldNotReturnErrorWhenMedicalExpensesNotSelected() {
        List<String> selectedRisks = List.of("Active Rest");
        List<String> countriesToVisit = null;

        TravelCalculatePremiumCoreCommand request = buildResult(selectedRisks, countriesToVisit);

        Optional<ValidationErrorDto> error = validateCountryForMedicalRisk.validate(request);

        assertFalse(error.isPresent());
    }

    @Test
    void shouldNotReturnErrorWhenMedicalExpensesSelectedAndCountryIsNotNull() {
        List<String> selectedRisks = List.of("Medical Expenses");
        List<String> countriesToVisit = List.of("SPAIN");

        TravelCalculatePremiumCoreCommand request = buildResult(selectedRisks, countriesToVisit);

        Optional<ValidationErrorDto> error = validateCountryForMedicalRisk.validate(request);

        assertFalse(error.isPresent());
    }

    @Test
    void shouldNotReturnErrorWhenNoRisksSelected() {
        TravelCalculatePremiumCoreCommand request = buildResult(null, null);

        Optional<ValidationErrorDto> error = validateCountryForMedicalRisk.validate(request);

        assertFalse(error.isPresent());
    }
}

