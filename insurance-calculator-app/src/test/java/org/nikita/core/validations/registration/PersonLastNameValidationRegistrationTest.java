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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonLastNameValidationRegistrationTest {

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    @InjectMocks
    private VLastNameRegistration validation;

    private RegistrationCommand request;

    private static final String ERROR_CODE_8 = "ERROR_CODE_8";
    private static final String ERROR_CODE_28 = "ERROR_CODE_28";
    private static final String ERROR_CODE_26 = "ERROR_CODE_26";
    private static final String ERROR_MSG_8 = "Field personLastName must not be empty!";
    private static final String ERROR_MSG_28 = "Field personLastName is too long!";
    private static final String ERROR_MSG_26 = "Field personLastName contains invalid characters!";

    @BeforeEach
    void setUp() {
        request = new RegistrationCommand();
    }

    @Test
    void shouldReturnErrorWhenPersonLastNameIsNull() {
        request.setLastName(null);
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_8)).thenReturn(ERROR_MSG_8);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("lastName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_8, errorOpt.get().description());
    }

    @Test
    void shouldReturnErrorWhenPersonLastNameIsEmpty() {
        request.setLastName("");
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_8)).thenReturn(ERROR_MSG_8);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("lastName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_8, errorOpt.get().description());
    }

    @Test
    void shouldReturnErrorWhenPersonLastNameIsLiteralNull() {
        request.setLastName("null");
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_8)).thenReturn(ERROR_MSG_8);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("lastName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_8, errorOpt.get().description());
    }

    @Test
    void shouldReturnErrorWhenPersonLastNameIsTooLong() {
        request.setLastName("ThisIsWayTooLongLastName");
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_28)).thenReturn(ERROR_MSG_28);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("lastName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_28, errorOpt.get().description());
    }

    @Test
    void shouldReturnErrorWhenPersonLastNameHasInvalidCharacters() {
        request.setLastName("Doe123");
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_26)).thenReturn(ERROR_MSG_26);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("lastName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_26, errorOpt.get().description());
    }

    @Test
    void shouldNotReturnErrorWhenPersonLastNameIsPresentAndValid() {
        request.setLastName("Doe");

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
    }
}
