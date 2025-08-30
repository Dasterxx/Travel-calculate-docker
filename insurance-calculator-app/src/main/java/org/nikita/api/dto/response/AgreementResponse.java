package org.nikita.api.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@XmlRootElement(name = "agreement")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgreementResponse {

    @XmlElement(name = "dateFrom")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime agreementDateFrom;

    @XmlElement(name = "dateTo")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime agreementDateTo;

    @XmlElementWrapper(name = "countriesToVisit")
    @XmlElement(name = "country")
    private List<String> countriesToVisit;

    @XmlElementWrapper(name = "selectedRisks")
    @XmlElement(name = "risk")
    private List<String> selectedRisks;

    @XmlElementWrapper(name = "passengers")
    @XmlElement(name = "passenger")
    @JsonAlias("persons")
    private List<PersonResponse> persons;

    @XmlElement(name = "uuid")
    @JsonProperty("uuid")
    private UUID uuid;
}
