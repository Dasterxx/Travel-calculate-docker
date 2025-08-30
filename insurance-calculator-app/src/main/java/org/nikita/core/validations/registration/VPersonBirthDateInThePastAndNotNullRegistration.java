package org.nikita.core.validations.registration;

import lombok.RequiredArgsConstructor;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.RegistrationFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class VPersonBirthDateInThePastAndNotNullRegistration implements RegistrationFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;

    @Override
    public Optional<ValidationErrorDto> validate(RegistrationCommand request) {
        LocalDate birthDate = request.getBirthDate();
        if (birthDate == null) {
            return Optional.of(new ValidationErrorDto(
                    "birthDate",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_22")
            ));
        } else if (birthDate.isAfter(LocalDate.now())) {
            return Optional.of(new ValidationErrorDto(
                    "birthDate",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_21")
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
