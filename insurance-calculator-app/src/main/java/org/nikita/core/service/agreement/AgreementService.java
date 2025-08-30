package org.nikita.core.service.agreement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.exceptions.AlreadyHasAnAgreement;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.mapper.AgreementMapper;
import org.nikita.core.repositories.AgreementRepository;
import org.nikita.core.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementValidationService agreementValidationService;
    private final AgreementEntityFactory agreementEntityFactory;
    private final AgreementPersonService agreementPersonService;
    private final AgreementMapper agreementMapper;
    private final PersonRepository personRepository;

    @Transactional
    public AgreementDto updateAgreement(UUID uuid, TravelCalculatePremiumCoreCommand command) {
        AgreementEntity existing = agreementRepository.findByUuid(uuid);
        if (existing == null) {
            throw new EntityNotFoundException("Agreement not found");
        }
        // Валидация
        agreementValidationService.validateAgreement(command);

        // Обновление полей
        AgreementEntity updatedEntity = agreementEntityFactory.updateAgreementEntity(existing, command);

        // Обновляем связь с пользователями, если нужно
        agreementPersonService.linkPersonsToAgreement(command.getAgreement().getPersons(), updatedEntity);

        AgreementEntity saved = agreementRepository.save(updatedEntity);

        return agreementMapper.toDto(saved);
    }

    @Transactional
    public void deleteAgreement(UUID uuid) {
        AgreementEntity agreement = agreementRepository.findByUuid(uuid);
        if (agreement == null) {
            throw new EntityNotFoundException("Agreement not found");
        }
        agreementRepository.delete(agreement);
    }


    @Transactional
    public TravelCalculatePremiumCoreCommand saveAgreement(TravelCalculatePremiumCoreCommand command) {
        agreementValidationService.validateAgreement(command);

        // Проверка: у каждого пользователя есть ли уже договор
        for (var personDto : command.getAgreement().getPersons()) {
            Optional<PersonEntity> personOpt = personRepository.findByPersonCode(personDto.getPersonCode());
            if (personOpt.isPresent() && personOpt.get().getAgreement() != null) {
                throw new AlreadyHasAnAgreement("User " + personOpt.get().getPersonCode() + " already has an agreement");
            }
        }

        AgreementEntity agreement = agreementEntityFactory.createAgreementEntity(command);

        agreementPersonService.linkPersonsToAgreement(command.getAgreement().getPersons(), agreement);

        AgreementEntity savedAgreement = agreementRepository.save(agreement);

        command.getAgreement().setUuid(savedAgreement.getUuid());

        return command;
    }

    public List<AgreementEntity> findAllByPersonCode(String personCode) {
        return agreementRepository.findAllByPersons_PersonCode(personCode);
    }


    public AgreementEntity findByUuid(UUID uuid) {
        return agreementRepository.findByUuid(uuid);
    }

    public List<AgreementEntity> findAll() {
        return agreementRepository.findAll();
    }
}
