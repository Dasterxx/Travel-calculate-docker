package org.nikita.core.validations.agreement.country;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.api.dto.ValidationErrorDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class CountryValidation implements TravelAgreementFieldValidation {

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        List<String> countriesToVisit = request.getAgreement().getCountriesToVisit();

        if (countriesToVisit == null
                || countriesToVisit.isEmpty()
                || countriesToVisit.stream().anyMatch(c -> c == null || c.isEmpty())) {
            return Optional.of(new ValidationErrorDto("countriesToVisit", "ERROR_CODE_23"));
        }

        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
