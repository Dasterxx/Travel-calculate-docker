package org.nikita.core.messagebroker.proposalack;

import lombok.extern.slf4j.Slf4j;

import org.nikita.core.messagebroker.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("postgresql-container")
class ProposalGenerationAckQueueListener {

    private final Integer totalRetryCount;

    private final JsonStringToProposalGenerationAckConverter proposalGenerationAckConverter;
    private final ProposalGenerationAckService proposalGenerationAckService;
    private final RabbitTemplate rabbitTemplate;

    ProposalGenerationAckQueueListener(@Value("${rabbitmq.total.retry.count:3}")
                                       Integer totalRetryCount,
                                       JsonStringToProposalGenerationAckConverter proposalGenerationAckConverter,
                                       ProposalGenerationAckService proposalGenerationAckService,
                                       RabbitTemplate rabbitTemplate) {
        this.totalRetryCount = totalRetryCount;
        this.proposalGenerationAckConverter = proposalGenerationAckConverter;
        this.proposalGenerationAckService = proposalGenerationAckService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK)
    public void receiveMessage(final Message message) throws Exception {
        try {
            processMessage(message);
        } catch (Exception e) {
            log.error("FAIL to process message: ", e);
            retryOrForwardToDeadLetterQueue(message);
        }
    }

    private void retryOrForwardToDeadLetterQueue(Message message) {
        Integer retryCount = message.getMessageProperties().getHeader("x-retry-count");
        log.info("MESSAGE DELIVERY TAG {} RETRY COUNT = {}", message.getMessageProperties().getDeliveryTag(), retryCount);
        if (retryCount == null) {
            retryCount = 0;
        }
        retryCount++;
        if (retryCount <= totalRetryCount) {
            // Update retry count and republish for retry
            message.getMessageProperties().setHeader("x-retry-count", retryCount);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK, message);
        } else {
            // Forward to DLQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_PROPOSAL_GENERATION_ACK_DLQ, message);
        }
    }

    private void processMessage(Message message) throws Exception {
        String messageBody = new String(message.getBody());
        log.info(messageBody);
        ProposalAckMessageGeneration proposalGenerationAck = proposalGenerationAckConverter.convert(messageBody);
        proposalGenerationAckService.process(proposalGenerationAck);
    }

}
