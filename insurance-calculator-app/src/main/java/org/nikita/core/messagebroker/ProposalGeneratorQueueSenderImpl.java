package org.nikita.core.messagebroker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.AgreementDto;
import org.nikita.core.messagebroker.proposalack.ProposalAckMessageDto;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Primary
@Profile({"postgresql-container"})
class ProposalGeneratorQueueSenderImpl implements ProposalGeneratorQueueSender {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void send(AgreementDto agreement) {
        try {
            String json = objectMapper.writeValueAsString(agreement);
            log.info("Sending message to RabbitMQ queue 'q.proposal-generation': {}", json);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION, json);
            log.info("Message sent successfully");
        } catch (JsonProcessingException e) {
            log.error("JSON conversion error", e);
        } catch (AmqpException e) {
            log.error("RabbitMQ send error", e);
        }
    }

    private static ProposalAckMessageDto getMessage(AgreementDto agreement, String proposalFilePath) {
        ProposalAckMessageDto ackMessage = new ProposalAckMessageDto();

        if (agreement.getPersons() != null && !agreement.getPersons().isEmpty()) {
            var person = agreement.getPersons().get(0);
            ackMessage.setPersonFirstName(person.getFirstName() != null ? person.getFirstName() : "unknown");
            ackMessage.setPersonLastName(person.getLastName() != null ? person.getLastName() : "unknown");
        } else {
            ackMessage.setPersonFirstName("unknown");
            ackMessage.setPersonLastName("unknown");
        }

        ackMessage.setDateFrom(agreement.getAgreementDateFrom());
        ackMessage.setDateTo(agreement.getAgreementDateTo());

        // Используем proposalFilePath, чтобы потом корректно сохранить в сущность
        ackMessage.setProposalFilePath(proposalFilePath);

        return ackMessage;
    }
}
