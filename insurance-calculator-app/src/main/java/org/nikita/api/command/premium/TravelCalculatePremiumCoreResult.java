package org.nikita.api.command.premium;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.ValidationErrorDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelCalculatePremiumCoreResult {

    private List<ValidationErrorDto> errors;

    private AgreementDto agreement;

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public TravelCalculatePremiumCoreResult(List<ValidationErrorDto> errors) {
        this.errors = errors;
    }

}
