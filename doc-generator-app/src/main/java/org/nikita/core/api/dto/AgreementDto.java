package org.nikita.core.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AgreementDto {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime agreementDateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime agreementDateTo;

    @Builder.Default
    private List<String> countriesToVisit = new ArrayList<>();

    private BigDecimal insuranceLimit;

    private BigDecimal agreementPremium;

    @Builder.Default
    private List<String> selectedRisks = new ArrayList<>();

    @JsonProperty("persons")
    @Builder.Default
    private List<PersonDto> persons = new ArrayList<>();

    private UUID uuid;

}
