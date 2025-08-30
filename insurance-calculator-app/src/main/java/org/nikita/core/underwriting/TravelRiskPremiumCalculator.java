package org.nikita.core.underwriting;

import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;

import java.math.BigDecimal;

public interface TravelRiskPremiumCalculator {

    BigDecimal calculatePremium(AgreementDto request, PersonDto person);

    String getRiskIc();

}
