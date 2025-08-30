package org.nikita.core.messagebroker;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String QUEUE_PROPOSAL_GENERATION = "q.proposal-generation";
    public static final String QUEUE_PROPOSAL_GENERATION_ACK = "q.proposal-generation-ack";
    public static final String QUEUE_PROPOSAL_GENERATION_ACK_DLQ = "q.proposal-generation-ack-dlq";

    @Bean
    public Queue createProposalGenerationQueue() {
        return new Queue(QUEUE_PROPOSAL_GENERATION);
    }

    @Bean
    public Queue createProposalGenerationAckQueue() {
        return new Queue(QUEUE_PROPOSAL_GENERATION_ACK);
    }

    @Bean
    public Queue createProposalGenerationAckDlq() {
        return new Queue(QUEUE_PROPOSAL_GENERATION_ACK_DLQ);
    }
}

