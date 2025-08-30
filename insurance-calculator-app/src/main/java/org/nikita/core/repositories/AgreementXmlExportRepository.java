package org.nikita.core.repositories;

import org.nikita.core.domain.AgreementXmlExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AgreementXmlExportRepository extends JpaRepository<AgreementXmlExportEntity, Long> {
    Optional<AgreementXmlExportEntity> findByAgreementUuid(UUID agreementUuid);

    boolean existsByAgreementUuid(UUID agreementUuid);

}
