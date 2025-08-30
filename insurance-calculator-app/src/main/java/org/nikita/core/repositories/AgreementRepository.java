package org.nikita.core.repositories;

import org.nikita.core.domain.AgreementEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AgreementRepository extends JpaRepository<AgreementEntity, Long> {
    @EntityGraph(attributePaths = {"persons"})
    AgreementEntity findByUuid(UUID uuid);

    List<AgreementEntity> findAll();

    @Query("select a.uuid from AgreementEntity a")
    List<UUID> findAllUuids();

    List<AgreementEntity> findAllByPersons_PersonCode(String personCode);

}

