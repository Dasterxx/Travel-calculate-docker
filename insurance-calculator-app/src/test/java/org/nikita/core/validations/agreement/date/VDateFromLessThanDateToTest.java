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
class VDateFromLessThanDateToTest {

    @InjectMocks
    private VDateFromLessThanDateTo validator;

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    private TravelCalculatePremiumCoreCommand request;
    private AgreementDto agreementResponse;

    private static final String ERROR_CODE_5 = "ERROR_CODE_5";
    private static final String ERROR_CODE_19 = "ERROR_CODE_19";
    private static final String ERROR_MSG_5 = "Field agreementDateFrom must be less than agreementDateTo!";
    private static final String ERROR_MSG_19 = "Both dates must be provided";

    @BeforeEach
    void setUp() {
        agreementResponse = new AgreementDto();
        request = new TravelCalculatePremiumCoreCommand();
        request.setAgreement(agreementResponse);
    }

    @Test
    void shouldReturnErrorForInvalidDates() {
        // dateFrom equals dateTo
        agreementResponse.setAgreementDateFrom(LocalDateTime.now());
        agreementResponse.setAgreementDateTo(LocalDateTime.now());
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_5)).thenReturn(ERROR_MSG_5);

        Optional<ValidationErrorDto> errorOpt = validator.validate(request);

        assertValidationError(errorOpt, "agreementDateFrom", ERROR_MSG_5);

        // dateFrom after dateTo
        agreementResponse.setAgreementDateFrom(LocalDateTime.now().plusDays(1));
        agreementResponse.setAgreementDateTo(LocalDateTime.now());
        errorOpt = validator.validate(request);

        assertValidationError(errorOpt, "agreementDateFrom", ERROR_MSG_5);
    }

    @Test
    void shouldReturnErrorForMissingDates() {
        agreementResponse.setAgreementDateFrom(null);
        agreementResponse.setAgreementDateTo(LocalDateTime.now());
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_19)).thenReturn(ERROR_MSG_19);

        Optional<ValidationErrorDto> errorOpt = validator.validate(request);

        assertValidationError(errorOpt, "agreementDateFrom", ERROR_MSG_19);

        agreementResponse.setAgreementDateFrom(LocalDateTime.now());
        agreementResponse.setAgreementDateTo(null);
        errorOpt = validator.validate(request);

        assertValidationError(errorOpt, "agreementDateFrom", ERROR_MSG_19);

        agreementResponse.setAgreementDateFrom(null);
        agreementResponse.setAgreementDateTo(null);
        errorOpt = validator.validate(request);

        assertValidationError(errorOpt, "agreementDateFrom", ERROR_MSG_19);
    }

    @Test
    void shouldNotReturnErrorForValidDates() {
        agreementResponse.setAgreementDateFrom(LocalDateTime.now());
        agreementResponse.setAgreementDateTo(LocalDateTime.now().plusDays(1));

        Optional<ValidationErrorDto> errorOpt = validator.validate(request);

        assertTrue(errorOpt.isEmpty());
    }

    private void assertValidationError(Optional<ValidationErrorDto> result,
                                       String expectedField,
                                       String expectedMessage) {
        assertTrue(result.isPresent());
        assertEquals(expectedField, result.get().errorCode());
        assertEquals(expectedMessage, result.get().description());
    }
}

