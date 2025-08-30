package org.nikita.core.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDto {
    private String personCode;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    @Builder.Default
    private List<RiskDto> risks = new ArrayList<>();
    private BigDecimal personPremium;
    private boolean blackListed;
}
