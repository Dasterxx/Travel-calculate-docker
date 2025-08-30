package org.nikita.core.validations.agreement;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmptySelectedRisksValidationTest {

    @Mock
    private ValidationErrorFactory errorCodeProperties;

    @InjectMocks
    private EmptySelectedRisksValidation validation;

    @Mock
    private TravelCalculatePremiumCoreCommand request;

    @Mock
    private AgreementDto agreementDto;

    private static final String ERROR_CODE = "ERROR_CODE_20";
    private static final String ERROR_MESSAGE = "Risk Type ic = No selected risks not supported!";

    @BeforeEach
    void setUp() {
        when(request.getAgreement()).thenReturn(agreementDto);
    }

    @Test
    @DisplayName("Should return error when selected risks is null")
    void shouldReturnErrorWhenSelectedRisksIsNull() {
        when(agreementDto.getSelectedRisks()).thenReturn(null);
        when(errorCodeProperties.getErrorDescription(eq(ERROR_CODE), anyList()))
                .thenReturn(ERROR_MESSAGE);

        var result = validation.validate(request);

        printErrorMessage(result);
        assertValidationError(result, "selectedRisks", ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Should return error when selected risks is empty")
    void shouldReturnErrorWhenSelectedRisksIsEmpty() {
        when(agreementDto.getSelectedRisks()).thenReturn(List.of());
        when(errorCodeProperties.getErrorDescription(eq(ERROR_CODE), anyList()))
                .thenReturn(ERROR_MESSAGE);

        var result = validation.validate(request);

        printErrorMessage(result);
        assertValidationError(result, "selectedRisks", ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Should not return error when selected risks are present")
    void shouldNotReturnErrorWhenSelectedRisksArePresent() {
        when(agreementDto.getSelectedRisks()).thenReturn(List.of("Risk1", "Risk2"));

        var result = validation.validate(request);

        assertTrue(result.isEmpty());
        verify(errorCodeProperties, never()).getErrorDescription(anyString(), anyList());
    }

    private static void printErrorMessage(Optional<ValidationErrorDto> result) {
        result.ifPresent(error -> System.out.printf("Validation error: code='%s', description='%s'%n",
                error.errorCode(), error.description()));
    }

    private static void assertValidationError(Optional<ValidationErrorDto> result, String expectedField, String expectedMessage) {
        assertAll("Validation error checks",
                () -> assertTrue(result.isPresent(), "Expected validation error to be present"),
                () -> {
                    var error = result.orElseThrow();
                    assertEquals(expectedField, error.errorCode(), "Error code mismatch");
                    assertEquals(expectedMessage, error.description(), "Error description mismatch");
                }
        );
    }
}
