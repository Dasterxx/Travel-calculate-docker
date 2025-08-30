package org.nikita.core.service;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.nikita.api.command.registration.RegistrationCommand;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.registration.UserUpdateRequest;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.Role;
import org.nikita.core.domain.VerificationToken;
import org.nikita.core.domain.mapper.PersonMapper;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.repositories.RefreshTokenRepository;
import org.nikita.core.repositories.RoleRepository;
import org.nikita.core.security.exceptions.PasswordException;
import org.nikita.core.service.register.EmailService;
import org.nikita.core.service.register.VerificationTokenService;
import org.nikita.core.util.PersonCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final PersonMapper personMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String generateUniquePersonCode() {
        try {
            return PersonCodeGenerator.generateUnique(
                    code -> !personRepository.existsByPersonCode(code),
                    10
            );
        } catch (IllegalStateException e) {
            throw new RuntimeException("Unable to generate unique person code after multiple attempts", e);
        }
    }

    public PersonEntity findOrCreatePerson(PersonDto dto) {
        PersonEntity existing = personRepository.findByFirstNameAndLastNameAndBirthDate(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getBirthDate()
        );

        if (existing != null) {
            return existing;
        }

        PersonEntity person = new PersonEntity();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setBirthDate(dto.getBirthDate());
        person.setPersonCode(generateUniquePersonCode());
        person.setBlackListed(false);

        try {
            return personRepository.save(person);
        } catch (DataIntegrityViolationException e) {
            return personRepository.findByFirstNameAndLastNameAndBirthDate(
                    dto.getFirstName(),
                    dto.getLastName(),
                    dto.getBirthDate()
            );
        }
    }

    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    @Transactional
    public PersonDto register(RegistrationCommand request) throws MessagingException {
        PersonEntity person = new PersonEntity();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setEmail(request.getEmail());
        person.setPassword(passwordEncoder.encode(request.getPassword()));
        person.setEnabled(false);
        person.setPersonCode(generateUniquePersonCode());
        person.setBirthDate(request.getBirthDate());
        person.setBlackListed(false);
        person.setGender(request.getGender());

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        person.getRoles().add(userRole);

        PersonEntity savedUser = personRepository.save(person);

        VerificationToken token = verificationTokenService.createVerificationToken(savedUser);
        emailService.sendVerificationHtmlEmail(savedUser, token.getToken());

        return personMapper.toResponseDto(savedUser);  // возвращаем DTO
    }

    @Transactional
    public PersonDto update(Long userId, UserUpdateRequest request) {
        PersonEntity user = findById(userId);

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        PersonEntity updated = personRepository.save(user);
        return personMapper.toResponseDto(updated);
    }


    @Transactional
    public void delete(Long userId, String password) {
        if (userId == null) {
            throw new EntityNotFoundException("User not found");
        }

        PersonEntity user = findById(userId);

        try {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new PasswordException("Incorrect password");
            }
            refreshTokenRepository.deleteByUserId(userId);

            personRepository.delete(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete user due to related data constraints", e);
        }
    }

    public PersonEntity findById(Long userId) {
        return personRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public PersonDto findUserDtoById(Long userId) {
        PersonEntity user = findById(userId);
        return personMapper.toResponseDto(user);
    }
}
