package org.nikita.core.underwriting;

import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;

import java.math.BigDecimal;
import java.util.List;

public interface ITravelPremiumUnderwriting {

    BigDecimal calculateTripCost(AgreementDto request, PersonDto person);
    BigDecimal calculateInsuranceCost(AgreementDto request, PersonDto person);

    List<RiskDto> calculateRiskPremiums(AgreementDto request, PersonDto person);

}
