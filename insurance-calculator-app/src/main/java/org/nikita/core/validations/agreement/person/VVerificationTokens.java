package org.nikita.core.validations.agreement.person;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class VVerificationTokens implements TravelAgreementFieldValidation {

    private final ValidationErrorFactory errorCodeProperties;

    public VVerificationTokens(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand command) {
        for (PersonDto person : command.getAgreement().getPersons()) {
            Optional<ValidationErrorDto> error = validateVerificationTokens(person);
            if (error.isPresent()) {
                return error;
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand command) {
        List<ValidationErrorDto> errors = new ArrayList<>();
        for (PersonDto person : command.getAgreement().getPersons()) {
            validateVerificationTokens(person).ifPresent(errors::add);
        }
        return errors;
    }

    private Optional<ValidationErrorDto> validateVerificationTokens(PersonDto person) {
        List<?> tokens = person.getVerificationTokens();
        if (tokens == null) {
            return Optional.of(new ValidationErrorDto(
                    "verificationTokens",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_37")
            ));
        }
        return Optional.empty();
    }
}
