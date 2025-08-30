package org.nikita.core.messagebroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProposalGeneratorQueueSenderStub implements ProposalGeneratorQueueSender {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void send(AgreementDto agreement) {
        try {
            log.info("ProposalGeneratorQueueSenderStub initialized");
            String json = objectMapper.writeValueAsString(agreement);
            log.info("Stub send to RabbitMQ: {}", json);
        } catch (JsonProcessingException e) {
            log.error("Error converting AgreementDto to JSON", e);
        }
    }
}
