package org.nikita.core.validations.registration;

import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.RegistrationFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
class RegistrationEmailValidation implements RegistrationFieldValidation {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    private final ValidationErrorFactory errorCodeProperties;

    public RegistrationEmailValidation(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(RegistrationCommand command) {
        String email = command.getEmail();
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return Optional.of(new ValidationErrorDto("email",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_33")));
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
