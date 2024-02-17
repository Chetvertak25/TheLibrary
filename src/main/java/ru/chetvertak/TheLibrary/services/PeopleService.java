package ru.chetvertak.TheLibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chetvertak.TheLibrary.models.Book;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.repositories.BooksRepositories;
import ru.chetvertak.TheLibrary.repositories.PeopleRepositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepositories peopleRepositories;
    private final BooksRepositories booksRepositories;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepositories peopleRepositories, BooksRepositories booksRepositories, PasswordEncoder passwordEncoder) {
        this.peopleRepositories = peopleRepositories;
        this.booksRepositories = booksRepositories;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAll(){
        return peopleRepositories.findAll();
    }


    public Person findOne(int id){
        Optional<Person> person = peopleRepositories.findById(id);
        return person.orElse(null);
    }

    public Person findOwnerById(int id){
        Optional<Book> book = booksRepositories.findById(id);
        if(book.isPresent()){
            Hibernate.initialize(book.get().getOwner());
            return book.get().getOwner();
        } else {
            return null;
        }
    }

    public Optional<Person> findByUsername(String username) {
        return peopleRepositories.findByUsername(username);
    }

    public List<Person> findByName(String name){
        return peopleRepositories.findByNameContainingIgnoreCase(name);
    }


    @Transactional
    public void save(Person person){
        peopleRepositories.save(person);
    }

    @Transactional
    public void update(int id, Person updatePerson){
        updatePerson.setId(id);
        updatePerson.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
        peopleRepositories.save(updatePerson);
    }
    @Transactional
    public void delete(int id){
        List<Book> books =booksRepositories.findByOwnerId(id);
        if (!books.isEmpty()){
            books.forEach(book -> {
                book.setTakeTime(null);
                book.setOwner(null);
                booksRepositories.save(book);
            });
        }
        peopleRepositories.deleteById(id);
    }


}