package org.nikita.api.registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotBlank(message = "First name must not be blank")
    @Size(max = 15, message = "First name must be at most 15 characters")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Size(max = 15, message = "Last name must be at most 15 characters")
    private String lastName;

    private String gender;
}
