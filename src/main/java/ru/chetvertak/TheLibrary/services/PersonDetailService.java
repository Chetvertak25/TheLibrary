package ru.chetvertak.TheLibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.repositories.PeopleRepositories;
import ru.chetvertak.TheLibrary.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {

    private final PeopleRepositories peopleRepositories;


    @Autowired
    public PersonDetailService(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepositories.findByUsername(s);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");

        }

        return new PersonDetails(person.get());
    }
}