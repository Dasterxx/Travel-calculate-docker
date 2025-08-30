package org.nikita.core.validations.agreement.date;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class VAgreementDateTo implements TravelAgreementFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;

    VAgreementDateTo(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        return (request.getAgreement().getAgreementDateTo() == null)
                ? Optional.of(new ValidationErrorDto("agreementDateTo", errorCodeProperties
                .getErrorDescription("ERROR_CODE_4")))
                : Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
