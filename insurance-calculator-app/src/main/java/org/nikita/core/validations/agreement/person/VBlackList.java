package org.nikita.core.validations.agreement.person;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.util.Placeholder;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.nikita.core.blacklist.service.BlackListPersonCheckService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class VBlackList implements TravelAgreementFieldValidation {

    private final ValidationErrorFactory errorCodeProperties;
    private final BlackListPersonCheckService blackListPersonCheckService;

    VBlackList(ValidationErrorFactory errorCodeProperties,
               BlackListPersonCheckService blackListPersonCheckService) {
        this.errorCodeProperties = errorCodeProperties;
        this.blackListPersonCheckService = blackListPersonCheckService;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        if (request.getAgreement().getPersons() == null || request.getAgreement().getPersons().isEmpty()) {
            // Можно не проверять черный список, если нет персон
            return Optional.empty();
        }

        for (PersonDto person : request.getAgreement().getPersons()) {
            if (person != null && blackListPersonCheckService.isPersonBlacklisted(person)) {
                Placeholder placeholder = new Placeholder("PERSON_LAST_NAME", person.getFirstName());
                errors.add(new ValidationErrorDto(
                        "blackListCheck",
                        errorCodeProperties.getErrorDescription("ERROR_CODE_30", List.of(placeholder))
                ));
            }
        }

        return errors.isEmpty() ? Optional.empty() : Optional.of(new ValidationErrorDto("validationErrors", errors.toString()));
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }

}
