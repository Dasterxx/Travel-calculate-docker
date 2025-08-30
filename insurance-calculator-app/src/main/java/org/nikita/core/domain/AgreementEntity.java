package org.nikita.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
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
@Entity
@XmlRootElement(name = "agreement")
@Table(name = "agreement")
public class AgreementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime agreementDateFrom;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime agreementDateTo;

    @ManyToMany
    @JoinTable(
            name = "agreement_countries",
            joinColumns = @JoinColumn(name = "agreement_id"),
            inverseJoinColumns = @JoinColumn(name = "country_id")
    )
    private List<CountryEntity> countriesToVisit = new ArrayList<>();

    private BigDecimal insuranceLimit;

    private BigDecimal agreementPremium;

    @ElementCollection
    @CollectionTable(name = "agreement_risks", joinColumns = @JoinColumn(name = "agreement_id"))
    @Column(name = "risk")
    private List<String> selectedRisks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "agreement")
    private List<PersonEntity> persons = new ArrayList<>();
}
