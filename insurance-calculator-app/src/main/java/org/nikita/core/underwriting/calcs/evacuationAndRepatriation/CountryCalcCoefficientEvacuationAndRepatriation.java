package org.nikita.core.underwriting.calcs.evacuationAndRepatriation;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
class CountryCalcCoefficientEvacuationAndRepatriation {

    public BigDecimal getCountryCoefficient(List<String> countries) {
        BigDecimal countryCoefficient = BigDecimal.ONE;
        for (String country : countries) {
            countryCoefficient = switch (country) {
                case "SPAIN" -> countryCoefficient.multiply(new BigDecimal("1.2"));
                case "USA" -> countryCoefficient.multiply(new BigDecimal("1.5"));
                case "JAPAN" -> countryCoefficient.multiply(new BigDecimal("3.5"));
                case "LATVIA" -> countryCoefficient.multiply(new BigDecimal("1.0"));
                default -> countryCoefficient;
            };
        }
        return countryCoefficient;
    }
}
