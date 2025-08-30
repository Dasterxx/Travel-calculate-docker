package org.nikita.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.mapper.PersonMapper;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.repositories.RoleRepository;
import org.nikita.core.security.exceptions.DeleteAnAdmin;
import org.nikita.core.security.exceptions.SelfDelete;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PersonMapper personMapper;

    @Transactional
    public List<PersonDto> findAllUsers() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(String personCode, String adminEmail) {
        PersonEntity user = personRepository.findByPersonCode(personCode)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getEmail().equals(adminEmail)) {
            throw new SelfDelete("You cannot delete yourself");
        }

        boolean isUserAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (isUserAdmin) {
            throw new DeleteAnAdmin("Admin cannot delete another admin");
        }

        personRepository.delete(user);
    }


    @Transactional
    public void setUserBlacklisted(String personCode, boolean blacklisted) {
        PersonEntity user = personRepository.findByPersonCode(personCode)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setBlackListed(blacklisted);
        personRepository.save(user);
    }

    @Transactional
    public PersonDto updateUser(String personCode, PersonDto dto) {
        PersonEntity entity = personRepository.findByPersonCode(personCode)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setBlackListed(dto.isBlackListed());
        entity.setGender(dto.getGender());
        entity.setEnabled(dto.isEnabled());

        PersonEntity saved = personRepository.save(entity);

        return personMapper.toResponseDto(saved);
    }
}

