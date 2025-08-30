package org.nikita.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.type.YesNoConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agreement_proposals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgreementProposalAckEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agreement_uuid", nullable = true)
    private UUID agreementUuid;

    @Column(name = "person_first_name", nullable = false)
    private String personFirstName;

    @Column(name = "person_last_name", nullable = false)
    private String personLastName;

    @Column(name = "date_from", nullable = false)
    private LocalDateTime dateFrom;

    @Column(name = "date_to", nullable = false)
    private LocalDateTime dateTo;

    @Column(name = "already_generated", nullable = false)
    @Convert(converter = YesNoConverter.class)
    private Boolean alreadyGenerated;

    @Column(name = "proposal_file_path", nullable = false)
    private String proposalFilePath;
}
