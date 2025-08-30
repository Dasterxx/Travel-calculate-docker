package org.nikita.core.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.blacklist.service.BlackListPersonCheckService;
import org.nikita.core.messagebroker.ProposalGeneratorQueueSender;
import org.nikita.core.underwriting.calcs.service.CalcPremiumForAgreement;
import org.nikita.core.validations.agreement.AgreementLevelValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TravelCalculatePremiumServiceImpl implements TravelCalculatePremiumService {

    private final AgreementLevelValidator requestValidator;
    private final ITravelResponseBuilder responseBuilder;
    private final CalcPremiumForAgreement calcPremiumForAgreement;
    private final ProposalGeneratorQueueSender proposalGeneratorQueueSender;
    private final BlackListPersonCheckService blackListPersonCheckService;

    @Override
    public TravelCalculatePremiumCoreResult calculatePremium(TravelCalculatePremiumCoreCommand command) {
        if (command == null || command.getAgreement() == null) {
            return responseBuilder.buildErrorResponse(
                    List.of(new ValidationErrorDto("INVALID_REQUEST", "Agreement is null"))
            );
        }
        return calculatePremium(command.getAgreement());
    }


    @Override
    public TravelCalculatePremiumCoreResult calculatePremium(AgreementDto request) {
        log.info("Calculating premium for agreement: {}", request);
        TravelCalculatePremiumCoreCommand command = TravelCalculatePremiumCoreCommand.builder()
                .agreement(request)
                .build();

        if (request == null) {
            return responseBuilder.buildErrorResponse(
                    List.of(new ValidationErrorDto("INVALID_REQUEST", "Agreement is null"))
            );
        }

        if (request.getPersons() != null) {
            for (PersonDto person : request.getPersons()) {
                boolean isBlacklisted = blackListPersonCheckService.isPersonBlacklisted(person);
                person.setBlackListed(isBlacklisted);
                if (isBlacklisted) {
                    log.warn("Person {} {} is blacklisted", person.getFirstName(), person.getLastName());
                    return buildBlackListErrorResponse(person);
                }
            }
        }

        List<ValidationErrorDto> errors = requestValidator.validateList(command);
        log.info("Validation result: {} errors found", errors.size());

        if (!errors.isEmpty()) {
            return responseBuilder.buildErrorResponse(errors);
        }
        calcPremiumForAgreement.calculatePremiumsForAgreement(command);
        proposalGeneratorQueueSender.send(command.getAgreement());

        if (command.getAgreement() == null) {
            return responseBuilder.buildErrorResponse(
                    List.of(new ValidationErrorDto("ERROR_CODE_31", "Agreement is null"))
            );
        }
        return responseBuilder.buildSuccessResponse(command.getAgreement());
    }

    private TravelCalculatePremiumCoreResult buildBlackListErrorResponse(PersonDto person) {
        String message = String.format("Person %s %s is blacklisted", person.getFirstName(), person.getLastName());
        ValidationErrorDto error = new ValidationErrorDto("blackListCheck", message);
        return responseBuilder.buildErrorResponse(List.of(error));
    }
}

