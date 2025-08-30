package org.nikita.core.messagebroker.proposalack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
class JsonStringToProposalGenerationAckConverter {

    private final ObjectMapper mapper;

    public JsonStringToProposalGenerationAckConverter() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public ProposalAckMessageGeneration convert(String json) throws JsonProcessingException {
        return mapper.readValue(json, ProposalAckMessageGeneration.class);
    }
}
