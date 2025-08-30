package org.nikita.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "COUNTRY_DEFAULT_DAY_RATE")
@Getter
@Setter
@NoArgsConstructor
public class CountryDefaultDayRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country_name", nullable = false, unique = true)
    private String countryName;

    @Column(name = "default_day_rate", nullable = false)
    private BigDecimal defaultDayRate;

    public CountryDefaultDayRate(String countryName, BigDecimal defaultDayRate) {
        this.countryName = countryName;
        this.defaultDayRate = defaultDayRate;
    }
}


