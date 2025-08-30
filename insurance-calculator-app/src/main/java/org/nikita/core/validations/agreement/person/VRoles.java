package org.nikita.core.validations.agreement.person;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Component
class VRoles implements TravelAgreementFieldValidation {

    private final ValidationErrorFactory errorCodeProperties;

    private static final Set<String> ALLOWED_ROLE_NAMES = Set.of("ROLE_USER", "ROLE_ADMIN");

    public VRoles(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand command) {
        for (PersonDto person : command.getAgreement().getPersons()) {
            Optional<ValidationErrorDto> error = validateRoles(person);
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
            validateRoles(person).ifPresent(errors::add);
        }
        return errors;
    }

    private Optional<ValidationErrorDto> validateRoles(PersonDto person) {
        Collection<String> roles = person.getRoles();
        if (roles == null || roles.isEmpty()) {
            return Optional.of(new ValidationErrorDto(
                    "roles",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_36")
            ));
        }

        for (String roleName : roles) {
            if (roleName == null || !ALLOWED_ROLE_NAMES.contains(roleName)) {
                return Optional.of(new ValidationErrorDto(
                        "roles",
                        errorCodeProperties.getErrorDescription("ERROR_CODE_35")
                ));
            }
        }

        return Optional.empty();
    }
}
