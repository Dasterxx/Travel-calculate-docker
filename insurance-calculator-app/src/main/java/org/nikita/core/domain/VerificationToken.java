package org.nikita.core.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nikita.core.util.SystemUtils;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private PersonEntity user;

    public VerificationToken(String token, PersonEntity user) {
        this.token = token;
        this.user = user;
        this.expirationDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 24 часа
    }

    public boolean isExpired() {
        return new Date().after(expirationDate);
    }
}
