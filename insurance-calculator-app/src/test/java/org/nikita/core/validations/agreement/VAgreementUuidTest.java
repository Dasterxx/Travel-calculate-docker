package org.nikita.core.validations.agreement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class VAgreementUuidTest {
    @Mock
    private ValidationErrorFactory errorCodeProperties;

    private VAgreementUuid validator;

    private TravelCalculatePremiumCoreCommand result;
    private AgreementDto agreementResponse;

    @BeforeEach
    void setUp() {
        validator = new VAgreementUuid(errorCodeProperties);
        result = new TravelCalculatePremiumCoreCommand();
        agreementResponse = new AgreementDto();
    }

    @Test
    void validUuid_returnsEmptyOptional() {

        agreementResponse.setUuid(UUID.randomUUID());
        result.setAgreement(agreementResponse);

        Optional<ValidationErrorDto> validationResult = validator.validate(result);

        assertTrue(validationResult.isEmpty(), "Expected no validation errors for valid UUID");
    }

    @Test
    void invalidUuidString_returnsErrorCode25() {
        when(errorCodeProperties.getErrorDescription("ERROR_CODE_25"))
                .thenReturn("Invalid UUID format");

        String invalidUuidString = "invalid-uuid-format";

        Optional<ValidationErrorDto> validationResult = validator.validateAgreementUuidString(invalidUuidString);

        assertTrue(validationResult.isPresent(), "Expected validation error for invalid UUID string");
        ValidationErrorDto error = validationResult.get();
        assertEquals("uuid", error.errorCode());
        assertEquals("Invalid UUID format", error.description());
    }

}