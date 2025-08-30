package org.nikita.core.validations.agreement.date;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
class VDateIsNotInThePast implements TravelAgreementFieldValidation {

    private final ValidationErrorFactory errorCodeProperties;

    VDateIsNotInThePast(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        LocalDateTime agreementDateFrom = request.getAgreement().getAgreementDateFrom();

        if (agreementDateFrom == null) {
            return Optional.of(new ValidationErrorDto
                    ("agreementDateFrom", "Must not be null!"));
        }

        return agreementDateFrom.isBefore(LocalDateTime.now())
                ? Optional.of(new ValidationErrorDto
                ("agreementDateFrom", errorCodeProperties.getErrorDescription("ERROR_CODE_1")))
                : Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
