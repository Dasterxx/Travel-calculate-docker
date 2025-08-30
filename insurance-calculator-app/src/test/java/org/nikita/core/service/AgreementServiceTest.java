package org.nikita.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.api.exceptions.ValidationException;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.repositories.AgreementRepository;
import org.nikita.core.service.agreement.AgreementEntityFactory;
import org.nikita.core.service.agreement.AgreementPersonService;
import org.nikita.core.service.agreement.AgreementService;
import org.nikita.core.service.agreement.AgreementValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AgreementServiceTest {

    @Autowired
    private AgreementService agreementService;

    @MockBean
    private AgreementRepository agreementRepository;

    @MockBean
    private AgreementValidationService agreementValidationService;

    @MockBean
    private AgreementEntityFactory agreementEntityFactory;

    @MockBean
    private AgreementPersonService agreementPersonService;

    private TravelCalculatePremiumCoreCommand command;
    private AgreementDto agreementDto;
    private AgreementEntity agreementEntity;

    @BeforeEach
    void setUp() {
        agreementDto = new AgreementDto();
        agreementDto.setCountriesToVisit(List.of("USA", "SPAIN"));
        agreementDto.setAgreementDateFrom(LocalDateTime.now().plusDays(1));
        agreementDto.setAgreementDateTo(LocalDateTime.now().plusDays(10));
        agreementDto.setInsuranceLimit(BigDecimal.valueOf(10000));
        agreementDto.setPersons(List.of(new PersonDto()));

        command = new TravelCalculatePremiumCoreCommand();
        command.setAgreement(agreementDto);

        agreementEntity = new AgreementEntity();
        agreementEntity.setUuid(UUID.randomUUID());

        // Мокаем фабрику: возвращает нашу сущность
        when(agreementEntityFactory.createAgreementEntity(any())).thenReturn(agreementEntity);
        // Мокаем репозиторий: возвращает ту же сущность, но с UUID
        when(agreementRepository.save(any(AgreementEntity.class))).thenReturn(agreementEntity);
    }

    @Test
    void testSaveAgreement_Success() {
        // Валидация ничего не выбрасывает
        doNothing().when(agreementValidationService).validateAgreement(any());

        TravelCalculatePremiumCoreCommand result = agreementService.saveAgreement(command);

        // Проверяем, что вызваны все нужные методы
        verify(agreementValidationService, times(1)).validateAgreement(command);
        verify(agreementEntityFactory, times(1)).createAgreementEntity(command);
        verify(agreementPersonService, times(1)).linkPersonsToAgreement(eq(agreementDto.getPersons()), eq(agreementEntity));
        verify(agreementRepository, times(1)).save(agreementEntity);

        assertNotNull(result.getAgreement().getUuid(), "UUID должен быть установлен");
        assertEquals(agreementEntity.getUuid(), result.getAgreement().getUuid());
    }

    @Test
    void testSaveAgreement_ValidationFails() {
        // Валидация выбрасывает исключение
        doThrow(new ValidationException("Validation failed", List.of(new ValidationErrorDto("field", "error"))))
                .when(agreementValidationService).validateAgreement(any());

        ValidationException ex = assertThrows(ValidationException.class, () -> agreementService.saveAgreement(command));
        assertTrue(ex.getErrors().stream().anyMatch(e -> e.errorCode().equals("field")));
        // Проверяем, что остальные методы не вызываются после ошибки
        verify(agreementEntityFactory, never()).createAgreementEntity(any());
        verify(agreementPersonService, never()).linkPersonsToAgreement(anyList(), any());
        verify(agreementRepository, never()).save(any());
    }
}
