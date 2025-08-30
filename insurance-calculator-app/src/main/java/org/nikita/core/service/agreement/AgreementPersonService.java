package org.nikita.core.service.agreement;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.service.PersonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementPersonService {

    private final PersonService personService;

    /**
     * Привязывает персон к договору, создаёт новых при необходимости.
     */
    public void linkPersonsToAgreement(List<PersonDto> personDtos, AgreementEntity agreement) {
        agreement.getPersons().clear();
        for (PersonDto personDto : personDtos) {
            PersonEntity person = personService.findOrCreatePerson(personDto);
            personDto.setPersonCode(person.getPersonCode());
            person.setAgreement(agreement);
            agreement.getPersons().add(person);
        }
    }
}

