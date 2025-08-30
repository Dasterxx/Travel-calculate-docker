package org.nikita.core.service;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreResult;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;

import java.util.List;

public interface ITravelResponseBuilder {
    TravelCalculatePremiumCoreResult buildErrorResponse(List<ValidationErrorDto> errors);
    TravelCalculatePremiumCoreResult buildSuccessResponse(AgreementDto agreement);
}
