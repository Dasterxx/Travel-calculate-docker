package org.nikita.core.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(
        name = "persons",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"first_name", "last_name", "person_code"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "person_code", nullable = false)
    private String personCode;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

//    @Column(name = "medical_risk_limit_level_ic")
//    private String medicalRiskLimitLevelIc;

    private String password;
    private boolean isEnabled;
    private String email;
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id")
    private AgreementEntity agreement;

    @Column(name = "black_listed", nullable = false)
    private Boolean blackListed;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE,
             CascadeType.REFRESH})
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<VerificationToken> verificationTokens = new ArrayList<>();

}
