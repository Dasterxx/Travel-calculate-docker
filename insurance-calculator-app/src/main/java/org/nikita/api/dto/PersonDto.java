package org.nikita.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.nikita.core.domain.VerificationToken;
import org.nikita.core.util.BigDecimalSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDto {

    @Pattern(regexp = "^[A-Z]-\\d{5}$|^$", message = "Must be X-12345 format")
    private String personCode;

    @NotBlank(message = "First name must not be blank")
    @Size(max = 15, message = "First name must be at most 15 characters")
    @Pattern(regexp = "^[A-Za-z\\s-]+$", message = "First name can contain only English letters, spaces, and hyphens")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 15, message = "Last name must be at most 15 characters")
    @Pattern(regexp = "^[A-Za-z\\s-]+$", message = "Last name can contain only English letters, spaces, and hyphens")
    private String lastName;

    @NotNull(message = "Birth date must not be null", groups = Create.class)
    @Past(message = "Birth date must be in the past", groups = Create.class)
    private LocalDate birthDate;

    private List<RiskDto> risks = new ArrayList<>();

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal personPremium;

    private boolean blackListed;

    @NotBlank(message = "Password must not be blank", groups = Create.class)
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @JsonProperty("isEnabled")
    private boolean isEnabled;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^(Male|Female|Other)?$", message = "Gender must be Male, Female, Other or empty")
    private String gender;

    private Collection<String> roles = new HashSet<>();

    private List<VerificationToken> verificationTokens = new ArrayList<>();

    public PersonDto(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public PersonDto(String firstName,
                     String lastName,
                     String personCode,
                     LocalDate birthDate,
                     Boolean blackListed,
                     String email,
                     String gender,
                     Collection<String> roles,
                     boolean isEnabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.blackListed = blackListed;
        this.personCode = personCode;
        this.email = email;
        this.gender = gender;
        this.roles = roles;
        this.isEnabled = isEnabled;
    }

    public interface Create {}
    public interface Update {}
}
