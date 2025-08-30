package org.nikita.web.v3;

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
import org.nikita.core.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.nikita.core.security.exceptions.FeedBackMessage.ERROR;
import static org.nikita.core.security.exceptions.FeedBackMessage.SUCCESS;

@CrossOrigin(origins = "${cors.allowed-origins}")
@RestController
@RequestMapping("/insurance/travel/web")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TravelInsuranceRestControllerV3 {

    private final TravelCalculatePremiumService service;
    private final AgreementService agreementService;
    private final AgreementMapper agreementMapper;

    @GetMapping("/initial-form-data")
    public ResponseEntity<ApiResponse> getInitialFormData() {
        try {
            AgreementDto agreement = new AgreementDto();
            agreement.getCountriesToVisit().add("");
            agreement.getPersons().add(new PersonDto());
            return ResponseEntity.ok(new ApiResponse(SUCCESS, agreement));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("error", "Server error"));
        }
    }

    @PostMapping("/v3")
    public ResponseEntity<ApiResponse> calculate(@RequestBody TravelCalculatePremiumCoreCommand request,
                                                 @RequestParam(value = "action", required = false) String action) {

        if ("clear".equals(action)) {
            return ResponseEntity.ok(new ApiResponse(SUCCESS, new TravelCalculatePremiumCoreResult()));
        }

        log.info("Processing travel insurance form submission for agreement: {}", request.getAgreement());

        TravelCalculatePremiumCoreResult response = service.calculatePremium(request);
        TravelCalculatePremiumCoreCommand savedResult = agreementService.saveAgreement(request);

        AgreementEntity savedAgreement = agreementService.findByUuid(savedResult.getAgreement().getUuid());
        AgreementDto updatedAgreementResponse = agreementMapper.toDto(savedAgreement);

        response.setAgreement(updatedAgreementResponse);

        return ResponseEntity.ok(new ApiResponse(SUCCESS, response));
    }

}
