package org.nikita.core.service.register;

import lombok.RequiredArgsConstructor;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.RegistrationFieldValidation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationValidationService {

    private final List<RegistrationFieldValidation> validators;

    public List<ValidationErrorDto> validate(RegistrationCommand command) {
        List<ValidationErrorDto> errors = new ArrayList<>();
        for (RegistrationFieldValidation validator : validators) {
            errors.addAll(validator.validateList(command));
        }
        return errors;
    }
}
