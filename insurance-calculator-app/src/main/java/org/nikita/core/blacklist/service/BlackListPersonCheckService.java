package org.nikita.core.blacklist.service;

import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.response.PersonResponse;

public interface BlackListPersonCheckService {

    boolean isPersonBlacklisted(PersonResponse personDTO);
    boolean isPersonBlacklisted(PersonDto personDTO);

}
