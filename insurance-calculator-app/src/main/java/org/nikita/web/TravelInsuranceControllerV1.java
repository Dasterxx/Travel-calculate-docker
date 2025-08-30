package org.nikita.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.api.exceptions.ValidationException;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.mapper.AgreementMapper;
import org.nikita.core.service.TravelCalculatePremiumService;
import org.nikita.core.service.agreement.AgreementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class TravelInsuranceControllerV1 {

    private final TravelCalculatePremiumService service;
    private final AgreementService agreementService;
    private final AgreementMapper agreementMapper;

    @GetMapping("/insurance/travel/web/v1")
    public String showForm(ModelMap modelMap) {
        AgreementDto agreement = new AgreementDto();
        PersonDto person = new PersonDto();

        agreement.getCountriesToVisit().add("");
        agreement.getPersons().add(person);
        modelMap.addAttribute("request",
                new TravelCalculatePremiumCoreCommand(agreement));
        return "travel-calculate-premium";
    }


    @PostMapping("/insurance/travel/web/v1")
    public String processForm(@ModelAttribute("request") TravelCalculatePremiumCoreCommand request,
                              @RequestParam(value = "action", required = false) String action,
                              ModelMap modelMap) {
        if ("clear".equals(action)) {
            return "redirect:/insurance/travel/web/v1";
        }
        log.info("Processing travel insurance form submission for agreement: {}", request.getAgreement());

        TravelCalculatePremiumCoreResult response;
        try {
            response = service.calculatePremium(request);
            TravelCalculatePremiumCoreCommand savedResult = agreementService.saveAgreement(request);

            AgreementEntity savedAgreement = agreementService.findByUuid(savedResult.getAgreement().getUuid());
            AgreementDto updatedAgreementResponse = agreementMapper.toDto(savedAgreement);


           // response.setAgreementResponse(updatedAgreementResponse);
            response.setAgreement(updatedAgreementResponse);
            log.info("Processing travel insurance form submission for agreement: {}", response.getAgreement());

        } catch (ValidationException ve) {
            log.warn("Validation failed: {}", ve.getErrors());
            response = new TravelCalculatePremiumCoreResult();
            response.setErrors(ve.getErrors());
            response.setAgreement(request.getAgreement());
        } catch (org.springframework.dao.DataIntegrityViolationException dive) {
            log.error("Database integrity violation", dive);
            response = new TravelCalculatePremiumCoreResult();
            response.setErrors(Collections.singletonList(new ValidationErrorDto(
                    "DUPLICATE_ENTRY",
                    "Person already exists in database"
            )));
            response.setAgreement(request.getAgreement());
        } catch (Exception e) {
            log.error("Unexpected error during premium calculation", e);
            response = new TravelCalculatePremiumCoreResult();
            response.setErrors(Collections.singletonList(new ValidationErrorDto(
                    "INTERNAL_ERROR",
                    "An unexpected error occurred. Please try again later."
            )));
            response.setAgreement(request.getAgreement());
        }

        modelMap.addAttribute("response", response);
        return "travel-calculate-premium";
    }

}
