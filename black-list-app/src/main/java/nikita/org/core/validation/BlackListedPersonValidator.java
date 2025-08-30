package nikita.org.core.validation;



import nikita.org.core.api.BlackListedPersonDTO;
import nikita.org.core.api.ValidationErrorDTO;

import java.util.List;

public interface BlackListedPersonValidator {

    List<ValidationErrorDTO> validate(BlackListedPersonDTO personDTO);

}