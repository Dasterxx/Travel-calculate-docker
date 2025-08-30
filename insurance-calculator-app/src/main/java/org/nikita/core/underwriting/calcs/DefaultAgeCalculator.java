package org.nikita.core.underwriting.calcs;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
class DefaultAgeCalculator implements AgeCalculator {
    @Override
    public Integer calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}