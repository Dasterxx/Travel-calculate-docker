package org.nikita.core.validations.registration;

import lombok.RequiredArgsConstructor;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.validations.RegistrationFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class VPersonDuplicateRegistration implements RegistrationFieldValidation {

    private final PersonRepository personRepository;
    private final ValidationErrorFactory errorCodeProperties;

    @Override
    public Optional<ValidationErrorDto> validate(RegistrationCommand command) {
        if (command.getFirstName() == null || command.getLastName() == null || command.getBirthDate() == null) {
            return Optional.empty(); // Нет данных для проверки
        }
        boolean exists = personRepository.existsByFirstNameAndLastNameAndBirthDate(
                command.getFirstName().trim(),
                command.getLastName().trim(),
                command.getBirthDate()
        );
        if (exists) {
            return Optional.of(new ValidationErrorDto(
                    "personDuplicate",
                    errorCodeProperties.getErrorDescription("ERROR_CODE_29")
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
