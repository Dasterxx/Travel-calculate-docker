package nikita.org.core.service;


import nikita.org.core.api.BlackListedPersonCoreCommand;
import nikita.org.core.api.BlackListedPersonCoreResult;
import nikita.org.core.api.BlackListedPersonDTO;
import nikita.org.core.api.ValidationErrorDTO;
import nikita.org.core.domain.BlackListedPersonEntity;
import nikita.org.core.repository.BlackListedPersonEntityRepository;
import nikita.org.core.validation.BlackListedPersonValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlackListedPersonServiceImplTest {

    @Mock private BlackListedPersonValidator personValidator;
    @Mock private BlackListedPersonEntityRepository repository;

    @InjectMocks
    private BlackListedPersonServiceImpl service;

    @Test
    void calculatePremium_withValidationErrors() {
        // Arrange
        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(new BlackListedPersonDTO());
        when(personValidator.validate(any(BlackListedPersonDTO.class)))
                .thenReturn(Collections.singletonList(new ValidationErrorDTO("ERROR_CODE_1", "Invalid")));

        // Act
        BlackListedPersonCoreResult result = service.check(command);

        // Assert
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> "ERROR_CODE_1".equals(e.getErrorCode())));
    }

    @Test
    void calculatePremium_personIsBlacklisted() {
        // Arrange
        BlackListedPersonDTO personDTO = new BlackListedPersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
//        personDTO.setPersonCode("123");

        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(personDTO);
        when(personValidator.validate(any(BlackListedPersonDTO.class))).thenReturn(Collections.emptyList());
        when(repository.findBy("John", "Doe")).thenReturn(Optional.of(new BlackListedPersonEntity()));

        // Act
        BlackListedPersonCoreResult result = service.check(command);

        // Assert
        assertTrue(result.getPersonDTO().getBlackListed());
    }

    @Test
    void calculatePremium_personIsNotBlacklisted() {
        // Arrange
        BlackListedPersonDTO personDTO = new BlackListedPersonDTO();
        personDTO.setFirstName("Jane");
        personDTO.setLastName("Doe");
//        personDTO.setPersonCode("456");

        BlackListedPersonCoreCommand command = new BlackListedPersonCoreCommand(personDTO);
        when(personValidator.validate(any(BlackListedPersonDTO.class))).thenReturn(Collections.emptyList());
        when(repository.findBy("Jane", "Doe")).thenReturn(Optional.empty());

        // Act
        BlackListedPersonCoreResult result = service.check(command);

        // Assert
        assertFalse(result.getPersonDTO().getBlackListed());
    }
}