package ru.chetvertak.TheLibrary.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.repositories.PeopleRepositories;
import ru.chetvertak.TheLibrary.util.Role;

@Service
@Transactional(readOnly = true)
public class RegistrationService {

    private final PeopleRepositories peopleRepositories;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepositories peopleRepositories, PasswordEncoder passwordEncoder) {
        this.peopleRepositories = peopleRepositories;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole(Role.ROLE_USER);
        peopleRepositories.save(person);
    }
}
