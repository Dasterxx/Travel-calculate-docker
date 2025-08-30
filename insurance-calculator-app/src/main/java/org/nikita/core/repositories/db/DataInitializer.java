package org.nikita.core.repositories.db;

import lombok.RequiredArgsConstructor;
import org.nikita.core.domain.CountryEntity;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.Role;
import org.nikita.core.repositories.CountryRepository;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_USER"));
            roleRepository.save(new Role("ROLE_ADMIN"));
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER role not found"));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN role not found"));

        if (personRepository.count() == 0) {
            PersonEntity user = new PersonEntity();
            user.setFirstName("Nikita");
            user.setLastName("Tsilikin");
            user.setPersonCode("U-76394");
            user.setBirthDate(LocalDate.of(2000, 8, 30));
            user.setPassword(passwordEncoder.encode("userpassword"));
            user.setEnabled(true);
            user.setEmail("tsilikin.2013@yandex.ru");
            user.setGender("MALE");
            user.setRoles(Set.of(userRole));
            user.setBlackListed(false);
            personRepository.save(user);

            PersonEntity user2 = new PersonEntity();
            user2.setFirstName("Oleg");
            user2.setLastName("Olegov");
            user2.setPersonCode("U-92730");
            user2.setBirthDate(LocalDate.of(2000, 4, 30));
            user2.setPassword(passwordEncoder.encode("userpassword"));
            user2.setEnabled(true);
            user2.setEmail("tsilikin.2014@gmail.com");
            user2.setGender("MALE");
            user2.setRoles(Set.of(userRole));
            user2.setBlackListed(false);
            personRepository.save(user2);

            PersonEntity admin = new PersonEntity();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setPersonCode("A-00001");
            admin.setBirthDate(LocalDate.of(1990, 1, 1));
            admin.setPassword(passwordEncoder.encode("adminpassword"));
            admin.setEnabled(true);
            admin.setEmail("admin@example.com");
            admin.setGender("MALE");
            admin.setRoles(Set.of(adminRole));
            admin.setBlackListed(false);
            personRepository.save(admin);
        }

        if (countryRepository.count() == 0) {
            countryRepository.save(new CountryEntity("SPAIN", "Spain"));
            countryRepository.save(new CountryEntity("USA", "United States"));
            countryRepository.save(new CountryEntity("JAPAN", "Japan"));
            countryRepository.save(new CountryEntity("LATVIA", "Latvia"));
        }
    }
}