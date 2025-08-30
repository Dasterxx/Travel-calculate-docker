package org.nikita.core.validations.agreement;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.core.util.Placeholder;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.nikita.core.validations.ValidationErrorFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class EmptySelectedRisksValidation implements TravelAgreementFieldValidation {
    private final ValidationErrorFactory errorCodeProperties;

    public EmptySelectedRisksValidation(ValidationErrorFactory errorCodeProperties) {
        this.errorCodeProperties = errorCodeProperties;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand request) {
        if (request.getAgreement() == null ||
                request.getAgreement().getSelectedRisks() == null ||
                request.getAgreement().getSelectedRisks().isEmpty()) {
            return Optional.of(new ValidationErrorDto(
                    "selectedRisks",
                    errorCodeProperties.getErrorDescription(
                            "ERROR_CODE_20",
                            List.of(new Placeholder("NO_SELECTED_RISKS", "No selected risks"))
                    )
            ));
        }
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand request) {
        return List.of();
    }
}
