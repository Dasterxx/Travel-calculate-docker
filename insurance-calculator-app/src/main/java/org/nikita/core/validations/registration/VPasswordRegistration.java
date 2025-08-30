package org.nikita.core.validations.registration;

import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.RegistrationFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class VPasswordRegistration implements RegistrationFieldValidation {

    private final ValidationErrorFactory errorCodeProperties;

    public VPasswordRegistration(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(RegistrationCommand command) {
        String password = command.getPassword();
        if (password == null || password.length() < 8) {
            return Optional.of(new ValidationErrorDto(
                    "password",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_32")
            ));
        }
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(RegistrationCommand command) {
        List<ValidationErrorDto> errors = new ArrayList<>();
        validate(command).ifPresent(errors::add);
        return errors;
    }
}
