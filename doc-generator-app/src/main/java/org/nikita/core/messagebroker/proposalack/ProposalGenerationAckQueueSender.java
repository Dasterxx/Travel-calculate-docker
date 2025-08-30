package org.nikita.core.messagebroker.proposalack;

import org.nikita.core.api.dto.AgreementDto;
import org.springframework.stereotype.Component;

@Component
public interface ProposalGenerationAckQueueSender {

    void send(AgreementDto agreement, String proposalFilePath);

}
