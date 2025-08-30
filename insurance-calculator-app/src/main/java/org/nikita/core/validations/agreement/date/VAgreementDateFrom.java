package org.nikita.core.validations.agreement.date;

import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
class VAgreementDateFrom implements TravelAgreementFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;

    VAgreementDateFrom(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        log.info("Validate agreementDateFrom");
        log.info("AgreementDateFrom is {}", request.getAgreement().getAgreementDateFrom());
        return (request.getAgreement().getAgreementDateFrom() == null)
                ? Optional.of(new ValidationErrorDto("agreementDateFrom",
                errorCodeProperties.getErrorDescription("ERROR_CODE_2")))
                : Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
