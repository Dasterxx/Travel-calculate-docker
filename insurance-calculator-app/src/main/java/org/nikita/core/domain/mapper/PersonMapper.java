package org.nikita.core.domain.mapper;

import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.response.PersonResponse;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.Role;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class PersonMapper {

    public PersonResponse toResponse(PersonEntity entity) {
        return new PersonResponse(
                entity.getFirstName(),
                entity.getLastName(),
                entity.getBirthDate(),
                entity.getPersonCode()
        );
    }

    public PersonDto toResponseDto(PersonEntity entity) {
        return new PersonDto(
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPersonCode(),
                entity.getBirthDate(),
                entity.getBlackListed(),
                entity.getEmail(),
                entity.getGender(),
                entity.getRoles() == null ? new HashSet<>() :
                        entity.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet()),
                entity.isEnabled()
        );
    }
}

