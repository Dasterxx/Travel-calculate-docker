package org.nikita.core.validations.agreement.country;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class CountryListValidator implements TravelAgreementFieldValidation {

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        if (request.getAgreement().getCountriesToVisit() == null || request.getAgreement().getCountriesToVisit().isEmpty()) {
            return Optional.of(new ValidationErrorDto(
                    "countriesToVisit",
                    "At least one country must be specified"
            ));
        }

        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}

