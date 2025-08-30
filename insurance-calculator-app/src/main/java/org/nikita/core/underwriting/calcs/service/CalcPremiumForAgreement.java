package org.nikita.core.underwriting.calcs.service;

import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CalcPremiumForAgreement {
    private final IRiskPremiumCalculator premiumCalculator;

    public CalcPremiumForAgreement(IRiskPremiumCalculator premiumCalculator) {
        this.premiumCalculator = premiumCalculator;
    }

    public void calculatePremiumsForAgreement(TravelCalculatePremiumCoreCommand command) {
        BigDecimal totalAgreementPremium = BigDecimal.ZERO;

        for (PersonDto person : command.getAgreement().getPersons()) {
            BigDecimal totalPersonPremium = BigDecimal.ZERO;
            List<RiskDto> riskDtos = new ArrayList<>();

            for (String riskIc : command.getAgreement().getSelectedRisks()) {
                BigDecimal riskPremium = premiumCalculator.calculateRiskPremium(command.getAgreement(), person, riskIc);
                riskDtos.add(new RiskDto(riskIc, riskPremium));
                totalPersonPremium = totalPersonPremium.add(riskPremium);
            }

            person.setRisks(riskDtos);
            person.setPersonPremium(totalPersonPremium);

            totalAgreementPremium = totalAgreementPremium.add(totalPersonPremium);
        }

        command.getAgreement().setAgreementPremium(totalAgreementPremium);
    }
}
