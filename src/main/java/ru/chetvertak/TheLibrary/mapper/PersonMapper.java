package ru.chetvertak.TheLibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.chetvertak.TheLibrary.dto.PersonDTO;
import ru.chetvertak.TheLibrary.config.models.Person;

@Component
public class PersonMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PersonMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Person convertToPerson(PersonDTO personDTO){
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertToPersonDTO(Person person){
        return modelMapper.map(person, PersonDTO.class);
    }
}
