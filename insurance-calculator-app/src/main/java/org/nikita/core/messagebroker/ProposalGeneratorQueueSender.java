package org.nikita.core.messagebroker;

import org.nikita.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

@Component
public interface ProposalGeneratorQueueSender {
    void send(AgreementDto message);
}
