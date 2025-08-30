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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class VAgreementDateToTest {

    @InjectMocks
    private VAgreementDateTo validation;

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    private TravelCalculatePremiumCoreCommand request;
    private AgreementDto agreementResponse;

    private static final String ERROR_CODE = "ERROR_CODE_4";
    private static final String ERROR_MESSAGE = "Field agreementDateTo must not be empty!";

    @BeforeEach
    void setUp() {
        agreementResponse = new AgreementDto();
        request = new TravelCalculatePremiumCoreCommand();
        request.setAgreement(agreementResponse);
    }

    @Test
    void shouldReturnErrorWhenAgreementDateToIsNull() {
        agreementResponse.setAgreementDateTo(null);
        when(errorCodeProperties.getErrorDescription(ERROR_CODE)).thenReturn(ERROR_MESSAGE);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("agreementDateTo", errorOpt.get().errorCode());
        assertEquals(ERROR_MESSAGE, errorOpt.get().description());
        verify(errorCodeProperties).getErrorDescription(ERROR_CODE);
    }

    @Test
    void shouldNotReturnErrorWhenAgreementDateToIsPresent() {
        agreementResponse.setAgreementDateTo(LocalDateTime.now());

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
        verifyNoInteractions(errorCodeProperties);
    }
}
