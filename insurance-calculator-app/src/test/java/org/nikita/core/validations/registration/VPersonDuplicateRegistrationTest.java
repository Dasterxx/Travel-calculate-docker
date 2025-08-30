package org.nikita.core.validations.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.ValidationErrorDto;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.validations.ValidationErrorFactory;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VPersonDuplicateRegistrationTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private ValidationErrorFactory errorCodeProperties;

    @InjectMocks
    private VPersonDuplicateRegistration validator;

    private RegistrationCommand command;

    @BeforeEach
    void setUp() {
        command = new RegistrationCommand();
        command.setFirstName("John");
        command.setLastName("Doe");
        command.setBirthDate(LocalDate.of(1990, 1, 1));
    }

    @Test
    void shouldReturnErrorWhenDuplicateExists() {
        when(personRepository.existsByFirstNameAndLastNameAndBirthDate("John", "Doe", LocalDate.of(1990, 1, 1)))
                .thenReturn(true);
        when(errorCodeProperties.getErrorDescription("ERROR_CODE_29"))
                .thenReturn("Duplicate person found");

        Optional<ValidationErrorDto> result = validator.validate(command);

        assertTrue(result.isPresent());
        assertEquals("personDuplicate", result.get().errorCode());
        assertEquals("Duplicate person found", result.get().description());
    }

    @Test
    void shouldNotReturnErrorWhenNoDuplicate() {
        when(personRepository.existsByFirstNameAndLastNameAndBirthDate("John", "Doe", LocalDate.of(1990, 1, 1)))
                .thenReturn(false);

        Optional<ValidationErrorDto> result = validator.validate(command);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotReturnErrorWhenDataIncomplete() {
        command.setFirstName(null);

        Optional<ValidationErrorDto> result = validator.validate(command);

        assertTrue(result.isEmpty());
        verifyNoInteractions(personRepository);
    }
}
