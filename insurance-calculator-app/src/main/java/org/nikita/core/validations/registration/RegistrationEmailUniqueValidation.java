package org.nikita.core.validations.registration;

import lombok.RequiredArgsConstructor;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.service.PersonService;
import org.nikita.core.validations.RegistrationFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class RegistrationEmailUniqueValidation implements RegistrationFieldValidation {

    private final PersonService personService;
    private final ValidationErrorFactory errorCodeProperties;

    @Override
    public Optional<ValidationErrorDto> validate(RegistrationCommand command) {
        String email = command.getEmail();
        if (email != null && personService.existsByEmail(email)) {
            return Optional.of(new ValidationErrorDto(
                    "email",
                    errorCodeProperties != null
                            ? errorCodeProperties.getErrorDescription("ERROR_CODE_40")
                            : "Email is already in use"
            ));
        }
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(RegistrationCommand command) {
        return validate(command).map(List::of).orElse(List.of());
    }
}
