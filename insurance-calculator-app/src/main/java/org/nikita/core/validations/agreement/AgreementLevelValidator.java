package org.nikita.core.validations.agreement;

import lombok.extern.slf4j.Slf4j;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.validations.TravelAgreementFieldValidation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class AgreementLevelValidator implements TravelAgreementFieldValidation {

    private final List<TravelAgreementFieldValidation> agreementValidations;

    public AgreementLevelValidator(List<TravelAgreementFieldValidation> agreementValidations) {
        this.agreementValidations = agreementValidations;
    }

    @Override
    public Optional<ValidationErrorDto> validate(TravelCalculatePremiumCoreCommand command) {
        // Можно оставить пустым или реализовать, если нужно
        return Optional.empty();
    }

    @Override
    public List<ValidationErrorDto> validateList(TravelCalculatePremiumCoreCommand command) {
        return agreementValidations.stream()
                .flatMap(validation -> {
                    List<ValidationErrorDto> listErrors = validation.validateList(command);
                    Optional<ValidationErrorDto> optionalError = validation.validate(command);
                    return Stream.concat(
                            listErrors.stream(),
                            optionalError.stream()
                    );
                })
                .collect(Collectors.toList());
    }

    // Для совместимости с сервисами:
    public List<ValidationErrorDto> validateAgreement(TravelCalculatePremiumCoreCommand command) {
        return validateList(command);
    }
}
