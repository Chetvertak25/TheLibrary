package ru.chetvertak.TheLibrary.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.chetvertak.TheLibrary.config.models.Person;
import ru.chetvertak.TheLibrary.services.PeopleService;

import java.time.LocalDateTime;

@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Person.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

        if (peopleService.findByUsername(person.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "Человек с таким логином уже существует!");
        }

        if (person.getYearOfBirth() < 1900 || person.getYearOfBirth() > LocalDateTime.now().getYear()) {
            errors.rejectValue("yearOfBirth", "", "Введите реальный год рождения!");
        } else if (person.getYearOfBirth() > 2010) {
            errors.rejectValue("yearOfBirth", "", "Человек должен быть старше 14 лет");
        }
    }
}
