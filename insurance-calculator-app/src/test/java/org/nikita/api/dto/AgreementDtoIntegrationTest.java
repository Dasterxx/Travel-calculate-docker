package org.nikita.api.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.exceptions.ValidationException;
import org.nikita.core.domain.Role;
import org.nikita.core.domain.VerificationToken;
import org.nikita.core.repositories.RoleRepository;
import org.nikita.core.service.agreement.AgreementValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Profile("test")
public class AgreementDtoIntegrationTest {

    @Autowired
    private AgreementValidationService agreementValidationService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private PersonDto person;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();

        person = new PersonDto();
        person.setPersonCode("A-12345");
        person.setFirstName("Vasja");
        person.setLastName("Pupkin");
        person.setBirthDate(LocalDate.of(2000, 1, 1));
        person.setBlackListed(false);
        person.setPassword("ValidPass123");
        person.setEmail("vasja@example.com");
        person.setGender("Male");
        person.setRoles(List.of(userRole.getName()));
        person.setVerificationTokens(List.of(new VerificationToken()));
    }

    @Test
    void shouldReturnErrorWhenAgreementDateFromIsNull() {
        TravelCalculatePremiumCoreCommand command = createCommand(
                null,
                LocalDateTime.parse("2030-01-01T00:00:00", DATE_TIME_FORMATTER),
                List.of("SPAIN")
        );

        ValidationException ex = assertThrows(ValidationException.class,
                () -> agreementValidationService.validateAgreement(command));

        assertEquals(3, ex.getErrors().size());
        assertEquals("agreementDateFrom", ex.getErrors().get(0).errorCode());
    }

    @Test
    void shouldReturnErrorWhenAgreementDateFromIsInThePast() {
        TravelCalculatePremiumCoreCommand command = createCommand(
                LocalDateTime.parse("2020-01-01T00:00:00", DATE_TIME_FORMATTER),
                LocalDateTime.parse("2030-01-01T00:00:00", DATE_TIME_FORMATTER),
                List.of("SPAIN")
        );

        ValidationException ex = assertThrows(ValidationException.class,
                () -> agreementValidationService.validateAgreement(command));

        assertEquals(1, ex.getErrors().size());
        assertEquals("agreementDateFrom", ex.getErrors().get(0).errorCode());
    }

    @Test
    void shouldReturnErrorWhenAgreementDateToIsNull() {
        TravelCalculatePremiumCoreCommand command = createCommand(
                LocalDateTime.parse("2028-01-01T00:00:00", DATE_TIME_FORMATTER),
                null,
                List.of("SPAIN")
        );

        ValidationException ex = assertThrows(ValidationException.class,
                () -> agreementValidationService.validateAgreement(command));

        assertEquals(2, ex.getErrors().size());
        assertEquals("agreementDateTo", ex.getErrors().get(0).errorCode());
    }

    @Test
    void shouldReturnErrorWhenCountryIsNull() {
        TravelCalculatePremiumCoreCommand command = createCommand(
                LocalDateTime.parse("2028-01-01T00:00:00", DATE_TIME_FORMATTER),
                LocalDateTime.parse("2030-01-01T00:00:00", DATE_TIME_FORMATTER),
                List.of()
        );

        ValidationException ex = assertThrows(ValidationException.class,
                () -> agreementValidationService.validateAgreement(command));

        assertEquals(2, ex.getErrors().size());
        assertEquals("countriesToVisit", ex.getErrors().get(0).errorCode());
    }

    private TravelCalculatePremiumCoreCommand createValidCommand() {
        return createCommand(
                LocalDateTime.parse("2028-01-01T00:00:00", DATE_TIME_FORMATTER),
                LocalDateTime.parse("2030-01-01T00:00:00", DATE_TIME_FORMATTER),
                List.of("SPAIN")
        );
    }

    private TravelCalculatePremiumCoreCommand createCommand(
            LocalDateTime from,
            LocalDateTime to,
            List<String> countries
    ) {
        AgreementDto agreement = AgreementDto.builder()
                .agreementDateFrom(from)
                .agreementDateTo(to)
                .countriesToVisit(countries)
                .selectedRisks(List.of("TRAVEL_MEDICAL"))
                .persons(List.of(person))
                .build();

        TravelCalculatePremiumCoreCommand command = new TravelCalculatePremiumCoreCommand();
        command.setAgreement(agreement);
        return command;
    }
}
