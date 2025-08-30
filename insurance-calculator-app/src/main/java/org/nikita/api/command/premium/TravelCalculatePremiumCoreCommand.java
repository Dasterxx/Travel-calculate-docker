package org.nikita.api.command.premium;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nikita.api.dto.AgreementDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelCalculatePremiumCoreCommand {

    /**
     * Raw input agreement DTO, typically used at the start of processing.
     */
    private AgreementDto agreement;

    /**
     * Returns true if AgreementDto is present.
     */
    public boolean hasAgreementDto() {
        return agreement != null;
    }
}
