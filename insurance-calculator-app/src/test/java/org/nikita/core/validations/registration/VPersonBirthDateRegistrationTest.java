package org.nikita.core.validations.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VPersonBirthDateRegistrationTest {

    @Mock private ValidationErrorFactory errorCodeProperties;
    @InjectMocks private VPersonBirthDateInThePastAndNotNullRegistration validation;
    private RegistrationCommand request;

    private static final String ERROR_CODE_21 = "ERROR_CODE_21";
    private static final String ERROR_CODE_22 = "ERROR_CODE_22";
    private static final String ERROR_MSG_21 = "Birthdate must be in past";
    private static final String ERROR_MSG_22 = "Birthdate required";

    @BeforeEach
    void setUp() {
        request = new RegistrationCommand();
    }

    @Test
    void shouldErrorWhenBirthdateNull() {
        request.setBirthDate(null);
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_22)).thenReturn(ERROR_MSG_22);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("birthDate", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_22, errorOpt.get().description());
    }

    @Test
    void shouldErrorWhenBirthdateInFuture() {
        request.setBirthDate(LocalDate.now().plusDays(1));
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_21)).thenReturn(ERROR_MSG_21);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("birthDate", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_21, errorOpt.get().description());
    }

    @Test
    void shouldPassWithValidBirthdate() {
        request.setBirthDate(LocalDate.now().minusYears(20));
        Optional<ValidationErrorDto> errorOpt = validation.validate(request);
        assertTrue(errorOpt.isEmpty());
    }
}
