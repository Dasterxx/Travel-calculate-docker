package org.nikita.core.validations.agreement;

import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.core.util.AvailableRisk;
import org.nikita.core.util.Placeholder;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
class ValidateSelectedRisks implements TravelAgreementFieldValidation {
    private final ValidationErrorFactory error;

    @Autowired
    public ValidateSelectedRisks(ValidationErrorFactory error) {
        this.error = error;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand agreement) {
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        List<String> selectedRisks = request.getAgreement().getSelectedRisks();
        log.info("ValidateSelectedRisks: Validating selected risks: {}", selectedRisks);

        if (selectedRisks == null || selectedRisks.isEmpty()) {
            log.warn("ValidateSelectedRisks: No risks selected. Returning error.");
            return List.of(new ValidationErrorDto("selectedRisks", error
                    .getErrorDescription("ERROR_CODE_24",
                            List.of(new Placeholder("NO_SELECTED_RISKS",
                                    "No selected risks")))));
        }

        List<ValidationErrorDto> errors = new ArrayList<>();

        for (String risk : selectedRisks) {
            boolean valid = AvailableRisk.isValidRisk(risk);
            log.info("Risk '{}' valid? {}", risk, valid);
            if (!valid) {
                log.error("Invalid risk found: {}", risk);
                errors.add(new ValidationErrorDto("selectedRisks",
                        error.getErrorDescription("ERROR_CODE_20", List.of(new Placeholder("NOT_EXISTING_RISK_TYPE", risk)))));
            }
        }

        if (!errors.isEmpty()) {
            log.warn("ValidateSelectedRisks: Found {} errors", errors.size());
            return errors;
        }

        log.info("ValidateSelectedRisks: Selected risks are valid.");
        return Collections.emptyList();
    }
}

