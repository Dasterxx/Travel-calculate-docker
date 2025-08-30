package org.nikita.core.service.agreement;

import lombok.RequiredArgsConstructor;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.api.exceptions.ValidationException;
import org.nikita.core.validations.agreement.AgreementLevelValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementValidationService {

    private final AgreementLevelValidator agreementLevelValidator;

    public void validateAgreement(TravelCalculatePremiumCoreCommand command) {
        List<ValidationErrorDto> errors = agreementLevelValidator.validateList(command);
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }
}
