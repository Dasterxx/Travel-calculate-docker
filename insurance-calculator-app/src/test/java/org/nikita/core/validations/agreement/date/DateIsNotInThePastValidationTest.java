package org.nikita.core.validations.agreement.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DateIsNotInThePastValidationTest {

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    @InjectMocks
    private VDateIsNotInThePast validation;

    private TravelCalculatePremiumCoreCommand request;

    private AgreementDto agreementResponse;

    private static final String ERROR_CODE = "ERROR_CODE_1";
    private static final String ERROR_MESSAGE = "Field agreementDateFrom must be in the future!";

    @BeforeEach
    void setUp() {
        agreementResponse = new AgreementDto();
        request = new TravelCalculatePremiumCoreCommand();
        request.setAgreement(agreementResponse);
    }

    @Test
    void shouldReturnErrorWhenAgreementDateFromIsInThePast() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        agreementResponse.setAgreementDateFrom(pastDate);

        when(errorCodeProperties.getErrorDescription(ERROR_CODE)).thenReturn(ERROR_MESSAGE);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertValidationError(errorOpt, "agreementDateFrom", ERROR_MESSAGE);
    }

    @Test
    void shouldReturnErrorWhenAgreementDateFromIsNull() {
        agreementResponse.setAgreementDateFrom(null);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertValidationError(errorOpt, "agreementDateFrom", "Must not be null!");
    }

    @Test
    void shouldNotReturnErrorWhenAgreementDateFromIsInTheFuture() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        agreementResponse.setAgreementDateFrom(futureDate);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
    }

    private void assertValidationError(Optional<ValidationErrorDto> result, String expectedField, String expectedMessage) {
        assertTrue(result.isPresent());
        assertEquals(expectedField, result.get().errorCode());
        assertEquals(expectedMessage, result.get().description());
    }
}
