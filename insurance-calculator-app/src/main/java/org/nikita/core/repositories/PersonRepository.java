package org.nikita.core.repositories;

import org.nikita.core.domain.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    boolean existsByFirstNameAndLastNameAndBirthDate(String firstName, String lastName, LocalDate birthDate);

    boolean existsByPersonCode(String personCode);

    PersonEntity findByFirstNameAndLastNameAndBirthDate(String personFirstName, String personLastName, LocalDate personBirthDate);

    boolean existsByEmail(String email);

    Optional<PersonEntity> findByEmail(String email);

    Optional<PersonEntity> findByPersonCode(String personCode);

}

