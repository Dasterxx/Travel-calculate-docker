package org.nikita.core.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "country")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", unique = true, nullable = false)
    private String code;

    @Column(name = "country_name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "countriesToVisit")
    private List<AgreementEntity> agreements = new ArrayList<>();


    public CountryEntity(String code, String name) {
        this.code = code;
        this.name = name;
    }
}

