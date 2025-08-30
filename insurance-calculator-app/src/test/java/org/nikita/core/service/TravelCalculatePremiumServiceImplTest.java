package org.nikita.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.blacklist.service.BlackListPersonCheckService;
import org.nikita.core.messagebroker.ProposalGeneratorQueueSender;
import org.nikita.core.underwriting.calcs.service.CalcPremiumForAgreement;
import org.nikita.core.validations.agreement.AgreementLevelValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
class TravelCalculatePremiumServiceImplTest {

    @Mock
    private AgreementLevelValidator agreementValidator;

    @Mock
    private ITravelResponseBuilder responseBuilder;

    @Mock
    private CalcPremiumForAgreement calcPremiumForAgreement;

    @Mock
    private ProposalGeneratorQueueSender proposalGeneratorQueueSender;

    @Mock
    private BlackListPersonCheckService blackListPersonCheckService;

    @InjectMocks
    private TravelCalculatePremiumServiceImpl premiumService;

    private AgreementDto agreement;
    private TravelCalculatePremiumCoreCommand command;

    @BeforeEach
    void setUp() {
        agreement = new AgreementDto();
        command = new TravelCalculatePremiumCoreCommand(agreement);
    }

    @Test
    void shouldCalculatePremiumSuccessfully() {
        agreement.setSelectedRisks(List.of("TRAVEL_MEDICAL"));
        PersonDto person = new PersonDto();
        agreement.setPersons(List.of(person));

        when(agreementValidator.validateList(any(TravelCalculatePremiumCoreCommand.class))).thenReturn(Collections.emptyList());

        // Мокируем проверку черного списка — персона не в черном списке
        when(blackListPersonCheckService.isPersonBlacklisted(person)).thenReturn(false);

        doAnswer(invocation -> {
            TravelCalculatePremiumCoreCommand command = invocation.getArgument(0);
            AgreementDto ag = command.getAgreement(); // Получаем AgreementDto из команды
            PersonDto p = ag.getPersons().get(0);

            // Ваша логика установки рисков и премий
            RiskDto risk = new RiskDto("TRAVEL_MEDICAL", BigDecimal.valueOf(1000));
            p.setRisks(List.of(risk));
            p.setPersonPremium(BigDecimal.valueOf(1000));
            ag.setAgreementPremium(BigDecimal.valueOf(1000));
            return null;
        }).when(calcPremiumForAgreement).calculatePremiumsForAgreement(any(TravelCalculatePremiumCoreCommand.class));


        when(responseBuilder.buildSuccessResponse(any(AgreementDto.class))).thenAnswer(invocation -> {
            AgreementDto ag = invocation.getArgument(0);
            TravelCalculatePremiumCoreResult result = new TravelCalculatePremiumCoreResult();
            result.setAgreement(ag);
            return result;
        });

        TravelCalculatePremiumCoreResult result = premiumService.calculatePremium(agreement);

        assertNotNull(result);
        assertFalse(result.hasErrors());
        assertNotNull(result.getAgreement());
        assertEquals(BigDecimal.valueOf(1000), result.getAgreement().getAgreementPremium());

        PersonDto resultPerson = result.getAgreement().getPersons().get(0);
        assertNotNull(resultPerson);
        assertEquals(BigDecimal.valueOf(1000), resultPerson.getPersonPremium());

        List<RiskDto> risks = resultPerson.getRisks();
        assertNotNull(risks);
        assertEquals(1, risks.size());
        assertEquals("TRAVEL_MEDICAL", risks.get(0).getRiskIc());
        assertEquals(BigDecimal.valueOf(1000), risks.get(0).getPremium());

//        verify(agreementValidator, times(1)).validate(command);
        verify(calcPremiumForAgreement).calculatePremiumsForAgreement(any(TravelCalculatePremiumCoreCommand.class));
        verify(responseBuilder, times(1)).buildSuccessResponse(agreement);
        verify(proposalGeneratorQueueSender, times(1)).send(agreement);
        verify(blackListPersonCheckService, times(1)).isPersonBlacklisted(person);
    }

    @Test
    void shouldReturnErrorWhenPersonIsBlacklisted() {
        PersonDto person = new PersonDto();
        person.setFirstName("Ivan");
        person.setLastName("Ivanov");
        agreement.setPersons(List.of(person));

        // Используем any() для проверки любого объекта PersonDto
        when(blackListPersonCheckService.isPersonBlacklisted(any(PersonDto.class))).thenReturn(true);

        String expectedMessage = "Person Ivan Ivanov is blacklisted";
        List<ValidationErrorDto> errors = List.of(
                new ValidationErrorDto("blackListCheck", expectedMessage)
        );

        when(responseBuilder.buildErrorResponse(errors))
                .thenReturn(new TravelCalculatePremiumCoreResult(errors));

        TravelCalculatePremiumCoreResult result = premiumService.calculatePremium(agreement);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertEquals("blackListCheck", result.getErrors().get(0).errorCode());
        assertTrue(result.getErrors().get(0).description().contains("Ivanov"));

        verify(blackListPersonCheckService, times(agreement.getPersons().size())).isPersonBlacklisted(any(PersonDto.class));
        verifyNoInteractions(agreementValidator, calcPremiumForAgreement, proposalGeneratorQueueSender);
    }

    @Test
    void shouldReturnErrorResponseWhenAgreementIsNull() {
        TravelCalculatePremiumCoreCommand nullCommand = new TravelCalculatePremiumCoreCommand(null);

        List<ValidationErrorDto> errors = List.of(new ValidationErrorDto("INVALID_REQUEST", "Agreement is null"));
        when(responseBuilder.buildErrorResponse(errors))
                .thenReturn(new TravelCalculatePremiumCoreResult(errors));

        TravelCalculatePremiumCoreResult result = premiumService.calculatePremium(nullCommand);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertEquals("INVALID_REQUEST", result.getErrors().get(0).errorCode());
        verifyNoInteractions(agreementValidator, calcPremiumForAgreement, blackListPersonCheckService);
    }


    @Test
    void shouldReturnValidationErrorsWhenValidatorFails() {
        List<ValidationErrorDto> errors = List.of(new ValidationErrorDto("ERR", "Error"));
        // Используем any() для аргумента в validate
        when(agreementValidator.validateList(any(TravelCalculatePremiumCoreCommand.class))).thenReturn(errors);
//        when(blackListPersonCheckService.isPersonBlacklisted(any(PersonDto.class))).thenReturn(false);
        when(responseBuilder.buildErrorResponse(errors))
                .thenReturn(new TravelCalculatePremiumCoreResult(errors));

        TravelCalculatePremiumCoreResult result = premiumService.calculatePremium(agreement);

        assertNotNull(result);
        assertTrue(result.hasErrors());
        assertEquals(1, result.getErrors().size());
        verify(calcPremiumForAgreement, never()).calculatePremiumsForAgreement(any());
        verify(blackListPersonCheckService, times(agreement.getPersons().size())).isPersonBlacklisted(any(PersonDto.class));
    }

}

