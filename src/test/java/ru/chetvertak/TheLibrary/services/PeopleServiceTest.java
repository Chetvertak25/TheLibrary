package ru.chetvertak.TheLibrary.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.chetvertak.TheLibrary.config.models.Book;
import ru.chetvertak.TheLibrary.config.models.Person;
import ru.chetvertak.TheLibrary.config.models.Role;
import ru.chetvertak.TheLibrary.repositories.BooksRepositories;
import ru.chetvertak.TheLibrary.repositories.PeopleRepositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PeopleServiceTest {

    @Mock
    private PeopleRepositories peopleRepositories;

    @Mock
    private BooksRepositories booksRepositories;

    @InjectMocks
    private PeopleService peopleService;

    private Book book1;

    private Book book2;
    private Book book3;

    private Person person1;

    private Person person2;

    private List<Book> books;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        book1 = new Book(1, "Test title1", "Test author1", 1233, LocalDateTime.now(), null, true);
        book2 = new Book(2, "Test title2", "Test author2", 4321, LocalDateTime.now(), null, true);
        book3 = new Book(3, "Test title3", "Test author3", 4231, null, null, false);

        person1 = new Person(1, "Test name1", 1234, List.of(book1, book2), "Test username1", "Test password1", Role.ROLE_USER);
        person2 = new Person(2, "Test name2", 4321, List.of(), "Test username2", "Test password2", Role.ROLE_ADMIN);

        book1.setOwner(person1);
        book2.setOwner(person1);

        books = List.of(book1, book2, book3);
    }

    @Test
    void findAll() {
        when(peopleRepositories.findAll()).thenReturn(List.of(person1, person2));

        List<Person> people = peopleService.findAll();

        assertEquals(2, people.size());
        assertEquals(person1, people.get(0));
        assertEquals(person2, people.get(1));
    }

    @Test
    void findOne() {
        when(peopleRepositories.findById(1)).thenReturn(Optional.of(person1));

        Person result = peopleService.findOne(1);

        assertEquals(1, result.getId());
        assertEquals("Test name1", result.getName());
    }

    @Test
    void findOwnerById() {
        when(booksRepositories.findById(1)).thenReturn(Optional.of(book1));

        Person person = peopleService.findOwnerById(1);

        assertEquals(person.getName(), "Test name1");
        assertEquals(person.getYearOfBirth(), 1234);
        assertEquals(person.getUsername(), "Test username1");
    }
    @Test
    void findOwnerByIdReturnNull() {
        when(booksRepositories.findById(1)).thenReturn(null);

        Person person = peopleService.findOwnerById(10);

        assertNull(person);

    }

    @Test
    void findByUsername() {
        when(peopleRepositories.findByUsername("Test username1")).thenReturn(Optional.of(person1));

        Person person = peopleService.findByUsername("Test username1").get();

        assertEquals(person.getName(), "Test name1");
        assertEquals(person.getYearOfBirth(), 1234);
        assertEquals(person.getUsername(), "Test username1");

    }

    @Test
    void findByName() {
        when(peopleRepositories.findByNameContainingIgnoreCase("Test name2")).thenReturn(List.of(person2));

        List<Person> result = peopleService.findByName("Test name2");

        assertEquals(1, result.size());
        assertEquals("Test name2", result.get(0).getName());
    }

    @Test
    void testSaveAndUpdateSuccess() {
        when(peopleRepositories.save(person1)).thenReturn(person1);

        boolean result = peopleService.save(person1);
        assertTrue(result);
    }

    @Test
    void testSaveAndUpdateFailure() {
        doThrow(new RuntimeException()).when(peopleRepositories).save(person1);

        boolean result = peopleService.save(person1);

        assertFalse(result);
    }

    @Test
    void testDeleteSuccess() {
        int id = 1;

        boolean result = peopleService.delete(id);

        assertTrue(result);
        verify(peopleRepositories).deleteById(id);
    }

    @Test
    void testDeleteFailure() {
        int id = 1;
        doThrow(new RuntimeException()).when(peopleRepositories).deleteById(id);

        boolean result = peopleService.delete(id);

        assertFalse(result);
    }
}