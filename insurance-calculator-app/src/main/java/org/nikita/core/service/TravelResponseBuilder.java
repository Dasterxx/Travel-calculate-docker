package org.nikita.core.service;

import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
class TravelResponseBuilder implements ITravelResponseBuilder{

    @Override
    public TravelCalculatePremiumCoreResult buildErrorResponse(List<ValidationErrorDto> errors) {
        log.warn("Building response with validation errors: {}", errors);
        TravelCalculatePremiumCoreResult result = new TravelCalculatePremiumCoreResult();
        result.setErrors(errors);
        result.setAgreement(new AgreementDto());
        return result;
    }

    @Override
    public TravelCalculatePremiumCoreResult buildSuccessResponse(AgreementDto agreement) {
        log.info("Building response with calculated premium for agreement: {}", agreement);
        TravelCalculatePremiumCoreResult result = new TravelCalculatePremiumCoreResult();
        result.setAgreement(agreement);
        return result;
    }

}
