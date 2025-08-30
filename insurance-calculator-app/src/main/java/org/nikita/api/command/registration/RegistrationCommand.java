package org.nikita.api.command.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCommand {

    @NotBlank(message = "First name must not be empty")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Birth date must not be null")
    private LocalDate birthDate;

    @Pattern(regexp = "Male|Female", message = "Gender must be Male or Female")
    private String gender;

    @JsonProperty("isEnabled")
    private boolean isEnabled;
}

