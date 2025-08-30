package org.nikita.core.underwriting.calcs.tripCancellation;

import lombok.RequiredArgsConstructor;
import org.nikita.core.underwriting.AvailableCountries;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CountrySafetyRatingCoefficient {

    private static final Map<AvailableCountries, Integer> countryRatings = Map.of(
            AvailableCountries.LATVIA, 5,
            AvailableCountries.SPAIN, 8,
            AvailableCountries.JAPAN, 9,
            AvailableCountries.USA, 6
    );

    public BigDecimal calculate(List<AvailableCountries> countries, BigDecimal travelCost) {
        int minRating = countries.stream()
                .map(countryRatings::get)
                .filter(Objects::nonNull)
                .min(Integer::compareTo)
                .orElse(5);

        return BigDecimal.ONE.divide(BigDecimal.valueOf(minRating), 4, RoundingMode.HALF_UP);
    }


}
