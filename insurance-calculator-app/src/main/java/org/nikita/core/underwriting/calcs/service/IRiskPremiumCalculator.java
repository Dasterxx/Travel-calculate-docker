package org.nikita.core.underwriting.calcs.service;

import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;

import java.math.BigDecimal;

public interface IRiskPremiumCalculator {
    BigDecimal calculateRiskPremium(AgreementDto agreement, PersonDto person, String riskIc);
}
