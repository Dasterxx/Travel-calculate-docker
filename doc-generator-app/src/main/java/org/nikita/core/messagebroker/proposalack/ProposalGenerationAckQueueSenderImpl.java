package org.nikita.core.messagebroker.proposalack;

import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.nikita.core.api.dto.AgreementDto;
import org.nikita.core.api.dto.ProposalAckMessageDto;
import org.nikita.core.messagebroker.RabbitMQConfig;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class ProposalGenerationAckQueueSenderImpl implements ProposalGenerationAckQueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void send(AgreementDto agreement, String proposalFilePath) {
        ProposalAckMessageDto ackMessage = getMessage(agreement, proposalFilePath);

        try {
            String json = objectMapper.writeValueAsString(ackMessage);
            log.info("PROPOSAL GENERATION ACK message content: {}", json);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK, json);
        } catch (JsonProcessingException e) {
            log.error("Error to convert proposal generation ack to JSON", e);
        } catch (AmqpException e) {
            log.error("Error to send proposal generation ack message", e);
        }
    }

    private static ProposalAckMessageDto getMessage(AgreementDto agreement, String proposalFilePath) {
        ProposalAckMessageDto ackMessage = new ProposalAckMessageDto();

        if (agreement.getPersons() != null && !agreement.getPersons().isEmpty()) {
            var person = agreement.getPersons().get(0);
            ackMessage.setFirstName(person.getFirstName() != null ? person.getFirstName() : "unknown");
            ackMessage.setLastName(person.getLastName() != null ? person.getLastName() : "unknown");
        } else {
            ackMessage.setFirstName("unknown");
            ackMessage.setLastName("unknown");
        }

        ackMessage.setDateFrom(agreement.getAgreementDateFrom());
        ackMessage.setDateTo(agreement.getAgreementDateTo());

        ackMessage.setProposalFilePath(proposalFilePath);
        return ackMessage;
    }
}