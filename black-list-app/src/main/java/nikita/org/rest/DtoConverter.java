package nikita.org.rest;

import nikita.org.core.api.BlackListedPersonCoreCommand;
import nikita.org.core.api.BlackListedPersonCoreResult;
import nikita.org.core.api.BlackListedPersonDTO;
import nikita.org.core.api.ValidationErrorDTO;
import nikita.org.dto.BlackListedPersonCheckRequest;
import nikita.org.dto.BlackListedPersonCheckResponse;
import nikita.org.dto.ValidationError;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoConverter {

    public BlackListedPersonCoreCommand buildCoreCommand(BlackListedPersonCheckRequest request) {
        BlackListedPersonDTO person = buildPerson(request);
        return new BlackListedPersonCoreCommand(person);
    }

    private BlackListedPersonDTO buildPerson(BlackListedPersonCheckRequest request) {
        BlackListedPersonDTO person = new BlackListedPersonDTO();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setPersonCode(request.getPersonCode());
        return person;
    }


    public BlackListedPersonCheckResponse buildResponse(BlackListedPersonCoreResult coreResult) {
        return coreResult.hasErrors()
                ? buildResponseWithErrors(coreResult.getErrors())
                : buildSuccessfulResponse(coreResult);
    }

    private BlackListedPersonCheckResponse buildResponseWithErrors(List<ValidationErrorDTO> coreErrors) {
        List<ValidationError> errors = transformValidationErrors(coreErrors);
        return new BlackListedPersonCheckResponse(errors);
    }

    private List<ValidationError> transformValidationErrors(List<ValidationErrorDTO> coreErrors) {
        return coreErrors.stream()
                .map(error -> new ValidationError(error.getErrorCode(), error.getDescription()))
                .toList();
    }

    private BlackListedPersonCheckResponse buildSuccessfulResponse(BlackListedPersonCoreResult coreResult) {
        BlackListedPersonDTO person = coreResult.getPersonDTO();
        BlackListedPersonCheckResponse response = new BlackListedPersonCheckResponse();
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());
        response.setPersonCode(person.getPersonCode());
        response.setBlacklisted(person.getBlackListed());
        return response;
    }

}
