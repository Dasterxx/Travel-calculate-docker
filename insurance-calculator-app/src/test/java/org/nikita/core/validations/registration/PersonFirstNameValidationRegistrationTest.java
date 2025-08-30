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
class PersonFirstNameValidationRegistrationTest {

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    @InjectMocks
    private VFirstNameRegistration validation;

    private RegistrationCommand request;

    private static final String ERROR_CODE_7 = "ERROR_CODE_7";
    private static final String ERROR_CODE_27 = "ERROR_CODE_27";
    private static final String ERROR_CODE_26 = "ERROR_CODE_26";
    private static final String ERROR_MSG_7 = "Field firstName must not be empty!";
    private static final String ERROR_MSG_27 = "Field firstName is too long!";
    private static final String ERROR_MSG_26 = "Field firstName contains invalid characters!";

    @BeforeEach
    void setUp() {
        request = new RegistrationCommand();
    }

    @Test
    void shouldReturnErrorWhenFirstNameIsNull() {
        request.setFirstName(null);
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_7)).thenReturn(ERROR_MSG_7);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("firstName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_7, errorOpt.get().description());
    }

    @Test
    void shouldReturnErrorWhenFirstNameIsEmpty() {
        request.setFirstName("");
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_7)).thenReturn(ERROR_MSG_7);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("firstName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_7, errorOpt.get().description());
    }

    @Test
    void shouldReturnErrorWhenFirstNameIsTooLong() {
        request.setFirstName("ThisIsWayTooLongFirstName");
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_27)).thenReturn(ERROR_MSG_27);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("firstName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_27, errorOpt.get().description());
    }

    @Test
    void shouldReturnErrorWhenFirstNameHasInvalidCharacters() {
        request.setFirstName("John123");
        when(errorCodeProperties.getErrorDescription(ERROR_CODE_26)).thenReturn(ERROR_MSG_26);

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isPresent());
        assertEquals("firstName", errorOpt.get().errorCode());
        assertEquals(ERROR_MSG_26, errorOpt.get().description());
    }

    @Test
    void shouldNotReturnErrorWhenFirstNameIsValid() {
        request.setFirstName("John");

        Optional<ValidationErrorDto> errorOpt = validation.validate(request);

        assertTrue(errorOpt.isEmpty());
    }
}
