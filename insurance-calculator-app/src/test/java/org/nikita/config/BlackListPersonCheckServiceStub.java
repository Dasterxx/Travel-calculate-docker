package org.nikita.config;

import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.response.PersonResponse;
import org.nikita.core.blacklist.service.BlackListPersonCheckService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class BlackListPersonCheckServiceStub implements BlackListPersonCheckService {
    @Override
    public boolean isPersonBlacklisted(PersonResponse personDTO) {
        return false;
    }

    @Override
    public boolean isPersonBlacklisted(PersonDto personDTO) {
        return false;
    }
}

