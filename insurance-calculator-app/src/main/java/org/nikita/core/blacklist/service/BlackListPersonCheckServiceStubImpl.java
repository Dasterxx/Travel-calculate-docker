package org.nikita.core.blacklist.service;

import lombok.extern.slf4j.Slf4j;

import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.response.PersonResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile({"h2"})
class BlackListPersonCheckServiceStubImpl implements BlackListPersonCheckService {

    @Override
    public boolean isPersonBlacklisted(PersonResponse personDTO) {
        log.info("BlackList stub invoked! Always return false!");
        return false;
    }

    @Override
    public boolean isPersonBlacklisted(PersonDto personDTO) {
        return false;
    }

}
