package org.nikita.core.messagebroker.proposalack;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.core.domain.AgreementProposalAckEntity;
import org.nikita.core.repositories.AgreementProposalAckEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ProposalGenerationAckService {

    private final AgreementProposalAckEntityRepository proposalAckEntityRepository;

    public void process(ProposalAckMessageGeneration proposalGenerationAck) {
        UUID agreementUuid = proposalGenerationAck.getAgreementUuid();
        String identifier = agreementUuid != null ? agreementUuid.toString() : "unknown-uuid";

        log.info("Start to process proposal ack: {}", identifier);

        try {
            AgreementProposalAckEntity ack = getAck(proposalGenerationAck);
            proposalAckEntityRepository.save(ack);
            log.info("Finish to process proposal ack: {}", identifier);
        } catch (Exception e) {
            log.error("Error saving AgreementProposalAckEntity for agreementUuid: {}", identifier, e);
            throw e;
        }
    }


    private static AgreementProposalAckEntity getAck(ProposalAckMessageGeneration proposalGenerationAck) {
        AgreementProposalAckEntity ack = new AgreementProposalAckEntity();

        ack.setAgreementUuid(proposalGenerationAck.getAgreementUuid());
        ack.setPersonFirstName(proposalGenerationAck.getPersonFirstName());
        ack.setPersonLastName(proposalGenerationAck.getPersonLastName());
        ack.setDateFrom(proposalGenerationAck.getDateFrom());
        ack.setDateTo(proposalGenerationAck.getDateTo());
        ack.setAlreadyGenerated(true);
        ack.setProposalFilePath(proposalGenerationAck.getProposalFilePath());

        return ack;
    }


}
