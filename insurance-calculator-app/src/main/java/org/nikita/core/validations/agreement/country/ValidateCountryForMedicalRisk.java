package org.nikita.core.validations.agreement.country;

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
class ValidateCountryForMedicalRisk implements TravelAgreementFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;

    public ValidateCountryForMedicalRisk(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        List<String> selectedRisks = request.getAgreement().getSelectedRisks();
        List<String> country = request.getAgreement().getCountriesToVisit();

        log.info("ValidateCountryForMedicalRisk: Validating country for medical risk. Selected risks: {}, Country : {}",
                selectedRisks, country);


        if (selectedRisks != null) {
            log.debug("ValidateCountryForMedicalRisk: selectedRisks = {}, country = {}", selectedRisks, country);
            if (selectedRisks.contains("Medical Expenses") && country == null) {
                log.warn("ValidateCountryForMedicalRisk: Medical Expenses selected but no country specified. Returning error.");
                return Optional.of(new ValidationErrorDto("country",
                        errorCodeProperties.getErrorDescription("ERROR_CODE_14")));
            }
        } else {
            log.debug("ValidateCountryForMedicalRisk: No risks selected, skipping country validation.");
        }

        log.info("ValidateCountryForMedicalRisk: Country validation passed.");
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
