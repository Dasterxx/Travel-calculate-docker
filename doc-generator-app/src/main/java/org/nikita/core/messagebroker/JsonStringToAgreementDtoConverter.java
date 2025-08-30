package org.nikita.core.messagebroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.nikita.core.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

@Component
public class JsonStringToAgreementDtoConverter {

    private final ObjectMapper mapper;

    public JsonStringToAgreementDtoConverter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public AgreementDto convert(String json) throws JsonProcessingException {
        return mapper.readValue(json, AgreementDto.class);
    }
}

