package org.nikita.core.validations.agreement;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
class VPersonCode implements TravelAgreementFieldValidation {

    private static final Pattern CODE_PATTERN = Pattern.compile("^[A-Z]-\\d{5}$");

    private final ValidationErrorFactory errorCodeProperties;

    public VPersonCode(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    private Optional<ValidationErrorDto> validatePersonCode(PersonDto person) {
        if (person.getPersonCode() == null || person.getPersonCode().isBlank()) {
            return Optional.of(new ValidationErrorDto(
                    "personCode",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_38")));
        }

        if (!CODE_PATTERN.matcher(person.getPersonCode()).matches()) {
            return Optional.of(new ValidationErrorDto(
                    "personCode",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_39")));
        }

        return Optional.empty();
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand command) {
        for (PersonDto person : command.getAgreement().getPersons()) {
            Optional<ValidationErrorDto> error = validatePersonCode(person);
            if (error.isPresent()) {
                return error;
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand command) {
        return command.getAgreement().getPersons().stream()
                .map(this::validatePersonCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}

