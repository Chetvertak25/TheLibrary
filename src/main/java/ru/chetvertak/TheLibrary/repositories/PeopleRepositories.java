package ru.chetvertak.TheLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.chetvertak.TheLibrary.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepositories extends JpaRepository<Person, Integer> {

    Person findOwnerById(int id);

    Optional<Person> findByUsername(String userName);
    List<Person> findByNameContainingIgnoreCase(String name);



}