package org.nikita.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "agreements_xml_export")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgreementXmlExportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agreement_uuid", nullable = false, length = 255, unique = true)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID agreementUuid;

    @Column(name = "already_exported", nullable = false)
    private boolean alreadyExported;

    public AgreementXmlExportEntity(UUID agreementUuid, boolean alreadyExported) {
        this.agreementUuid = agreementUuid;
        this.alreadyExported = alreadyExported;
    }

}
