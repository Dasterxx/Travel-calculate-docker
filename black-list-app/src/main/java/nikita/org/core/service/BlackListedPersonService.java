package nikita.org.core.service;

import nikita.org.core.api.BlackListedPersonCoreCommand;
import nikita.org.core.api.BlackListedPersonCoreResult;

public interface BlackListedPersonService {

    BlackListedPersonCoreResult check(BlackListedPersonCoreCommand command);

}
