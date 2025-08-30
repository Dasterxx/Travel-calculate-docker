package nikita.org.core.validation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nikita.org.core.api.BlackListedPersonDTO;
import nikita.org.core.api.ValidationErrorDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class BlackListedPersonValidatorImpl implements BlackListedPersonValidator {

    private final ValidationErrorFactory errorFactory;

    @Override
    public List<ValidationErrorDTO> validate(BlackListedPersonDTO personDTO) {
        List<ValidationErrorDTO> errors = new ArrayList<>();
        validatePersonFirstName(personDTO).ifPresent(errors::add);
        validatePersonLastName(personDTO).ifPresent(errors::add);
        validatePersonCode(personDTO).ifPresent(errors::add);

        return errors;
    }


    private Optional<ValidationErrorDTO> validatePersonFirstName(BlackListedPersonDTO personDTO) {
        return (personDTO.getFirstName() == null || personDTO.getFirstName().isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_1"))
                : Optional.empty();
    }

    private Optional<ValidationErrorDTO> validatePersonLastName(BlackListedPersonDTO personDTO) {
        return (personDTO.getLastName() == null || personDTO.getLastName().isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_2"))
                : Optional.empty();
    }

    private Optional<ValidationErrorDTO> validatePersonCode(BlackListedPersonDTO personDTO) {
        return (personDTO.getPersonCode() == null || personDTO.getPersonCode().isEmpty())
                ? Optional.of(errorFactory.buildError("ERROR_CODE_3"))
                : Optional.empty();
    }

}