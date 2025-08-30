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
class VDateFromLessThanDateTo implements TravelAgreementFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;

    VDateFromLessThanDateTo(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        LocalDateTime dateFrom = request.getAgreement().getAgreementDateFrom();
        LocalDateTime dateTo = request.getAgreement().getAgreementDateTo();

        if (dateFrom == null || dateTo == null) {
            return Optional.of(new ValidationErrorDto("agreementDateFrom",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_19")));
        }

        if (dateFrom.equals(dateTo) || dateFrom.isAfter(dateTo)) {
            return Optional.of(new ValidationErrorDto("agreementDateFrom",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_5")));
        }

        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
