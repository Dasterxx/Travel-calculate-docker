package org.nikita.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String personCode;
}
