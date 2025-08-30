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
class VLastNameRegistration implements RegistrationFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;
    private static final String NAME_PATTERN = "^[A-Za-z\\s-]+$";

    public VLastNameRegistration(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(RegistrationCommand request) {
        String lastName = request.getLastName();
        if (lastName == null || lastName.isEmpty() || lastName.equals("null")) {
            return Optional.of(new ValidationErrorDto(
                    "lastName",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_8")
            ));
        }
        if (lastName.length() > 15) {
            return Optional.of(new ValidationErrorDto(
                    "lastName",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_28")
            ));
        }
        if (!lastName.matches(NAME_PATTERN)) {
            return Optional.of(new ValidationErrorDto(
                    "lastName",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_26")
            ));
        }
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(RegistrationCommand request) {
        List<ValidationErrorDto> errors = new ArrayList<>();
        validate(request).ifPresent(errors::add);
        return errors;
    }
}
