package org.nikita.core.blacklist.service;

import lombok.extern.slf4j.Slf4j;

import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.response.PersonResponse;
import org.nikita.core.blacklist.dto.BlackListedPersonCheckRequest;
import org.nikita.core.blacklist.dto.BlackListedPersonCheckResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@Profile({"!test", "!h2"})
@Primary
//@Profile({"postgresql-container", "postgresql"})
class BlackListPersonCheckServiceImpl implements BlackListPersonCheckService {

    private final String personBlacklistedCheckUrl;

    private final RestTemplate restTemplate;

    BlackListPersonCheckServiceImpl(@Value("${person.blacklisted.check.url}")
                                    String personBlacklistedCheckUrl,
                                    RestTemplate restTemplate) {
        this.personBlacklistedCheckUrl = personBlacklistedCheckUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean isPersonBlacklisted(PersonResponse personDTO) {
     //   log.info("Blacklisted check for person with code {} started!", personDTO.getPersonCode());

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        BlackListedPersonCheckRequest request = new BlackListedPersonCheckRequest();
        request.setPersonFirstName(personDTO.getFirstName());
        request.setPersonLastName(personDTO.getLastName());
      //  request.setPersonCode(personDTO.getPersonCode());

        // Create an HttpEntity object with the request body and headers
        HttpEntity<BlackListedPersonCheckRequest> requestEntity = new HttpEntity<>(request, headers);

        // Make the POST request and expect a BlackListedPersonCheckResponse object in response
        ResponseEntity<BlackListedPersonCheckResponse> responseEntity = restTemplate.postForEntity(personBlacklistedCheckUrl, requestEntity, BlackListedPersonCheckResponse.class);

        BlackListedPersonCheckResponse response = responseEntity.getBody();

        if (response == null || response.getBlacklisted() == null) {
            log.warn("Blacklisted check response or blacklisted field is null for person {}", personDTO.getLastName());
            return false;
        }

    //    log.info("Blacklisted check for person with code {} return {}", personDTO.getPersonCode(), response.getBlacklisted());

        return response.getBlacklisted();
    }

    @Override
    public boolean isPersonBlacklisted(PersonDto personDTO) {
        return false;
    }

}
