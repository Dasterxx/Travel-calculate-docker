package nikita.org.core.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import nikita.org.core.api.BlackListedPersonCoreCommand;
import nikita.org.core.api.BlackListedPersonCoreResult;
import nikita.org.core.api.BlackListedPersonDTO;
import nikita.org.core.api.ValidationErrorDTO;
import nikita.org.core.repository.BlackListedPersonEntityRepository;
import nikita.org.core.validation.BlackListedPersonValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class BlackListedPersonServiceImpl implements BlackListedPersonService {

    private final BlackListedPersonValidator personValidator;
    private final BlackListedPersonEntityRepository repository;


    @Override
    public BlackListedPersonCoreResult check(BlackListedPersonCoreCommand command) {
        List<ValidationErrorDTO> errors = personValidator.validate(command.getPersonDTO());
        if (errors.isEmpty()) {
            BlackListedPersonDTO personDTO = command.getPersonDTO();
            boolean isBlacklisted = repository.findBy(
                    personDTO.getFirstName(),
                    personDTO.getLastName()
            ).isPresent();
            personDTO.setBlackListed(isBlacklisted);
            return new BlackListedPersonCoreResult(personDTO);
        } else {
            return new BlackListedPersonCoreResult(errors);
        }
    }
}
