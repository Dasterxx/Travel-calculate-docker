package org.nikita.core.validations.agreement;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
class VAgreementUuid implements TravelAgreementFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;

    public VAgreementUuid(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    private Optional<ValidationErrorDto> validateAgreementUuid(TravelCalculatePremiumCoreCommand agreement) {
        try {
            agreement.getAgreement().getUuid();
        } catch (IllegalArgumentException e) {
            return Optional.of(new ValidationErrorDto("uuid",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_25")));
        }

        return Optional.empty();
    }
    //for tests
    public Optional<ValidationErrorDto> validateAgreementUuidString(String uuidStr) {
        try {
            UUID.fromString(uuidStr);
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            return Optional.of(new ValidationErrorDto("uuid",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_25")));
        }
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand agreement) {
        return validateAgreementUuid(agreement);
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
