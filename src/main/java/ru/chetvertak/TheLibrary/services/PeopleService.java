package ru.chetvertak.TheLibrary.services;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chetvertak.TheLibrary.dto.PersonDTO;
import ru.chetvertak.TheLibrary.models.Book;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.repositories.BooksRepositories;
import ru.chetvertak.TheLibrary.repositories.PeopleRepositories;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepositories peopleRepositories;
    private final BooksRepositories booksRepositories;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleService(PeopleRepositories peopleRepositories, BooksRepositories booksRepositories, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.peopleRepositories = peopleRepositories;
        this.booksRepositories = booksRepositories;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
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
    public boolean save(Person person){
        try {
            peopleRepositories.save(person);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean update(int id, Person updatePerson){
        try {
            updatePerson.setId(id);
            updatePerson.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
            peopleRepositories.save(updatePerson);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Transactional
    public boolean delete(int id){
        try {
            List<Book> books = booksRepositories.findByOwnerId(id);
            if (!books.isEmpty()){
                books.forEach(book -> {
                    book.setTakeTime(null);
                    book.setOwner(null);
                    booksRepositories.save(book);
                });
            }
            peopleRepositories.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private PersonDTO convertToPersonDTO(Person person){
        return modelMapper.map(person, PersonDTO.class);
    }


}