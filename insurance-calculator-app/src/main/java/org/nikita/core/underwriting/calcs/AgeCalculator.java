package org.nikita.core.underwriting.calcs;

import java.time.LocalDate;

public interface AgeCalculator {
    Integer calculateAge(LocalDate birthDate);
}