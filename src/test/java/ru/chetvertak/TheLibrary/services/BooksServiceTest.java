package ru.chetvertak.TheLibrary.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.chetvertak.TheLibrary.models.Book;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.models.Role;
import ru.chetvertak.TheLibrary.repositories.BooksRepositories;
import ru.chetvertak.TheLibrary.repositories.PeopleRepositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BooksServiceTest {

    @Mock
    private BooksRepositories booksRepositories;

    @Mock
    private PeopleRepositories peopleRepositories;

    @InjectMocks
    private BooksService booksService;

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

        books = List.of(book1, book2, book3);
    }


    @Test
    public void testFindBooksById() {
        when(peopleRepositories.findById(1)).thenReturn(Optional.of(person1));

        List<Book> result = booksService.findBooksById(1);

        assertEquals(2, result.size());
        assertEquals("Test title1", result.get(0).getTitle());
        assertEquals("Test title2", result.get(1).getTitle());
    }


    @Test
    void findOne() {
        when(booksRepositories.findById(1)).thenReturn(Optional.of(book1));

        Book result = booksService.findOne(1);

        assertEquals(1, result.getId());
        assertEquals("Test title1", result.getTitle());

    }

    @Test
    void getAllBooks() {
        when(booksRepositories.findAll()).thenReturn(books);

        List<Book> findBooks = booksService.getAllBooks(0, 0, false);

        assertEquals(3, findBooks.size());
        assertEquals(book1, findBooks.get(0));
        assertEquals(book2, findBooks.get(1));
        assertEquals(book3, findBooks.get(2));
    }

    @Test
    void searchByTitle() {
        when(booksRepositories.findByTitleContainingIgnoreCase("title2")).thenReturn(List.of(book2));

        List<Book> result = booksService.searchByTitle("title2");

        assertEquals(1, result.size());
        assertEquals("Test title2", result.get(0).getTitle());
    }

    @Test
    void testSaveAndUpdateSuccess() {
        when(booksRepositories.save(book1)).thenReturn(book1);

        boolean result = booksService.save(book1);
        assertTrue(result);
    }

    @Test
    void testSaveAndUpdateFailure() {
        doThrow(new RuntimeException()).when(booksRepositories).save(book1);

        boolean result = booksService.save(book1);

        assertFalse(result);
    }

    @Test
    void assignOwnerToBook() {
        when(booksRepositories.findById(1)).thenReturn(Optional.of(book1));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        boolean result = booksService.assignOwnerToBook(1, person1);

        assertEquals(book1.getOwner(), person1);
        assertEquals(book1.getTakeTime().format(formatter), LocalDateTime.now().format(formatter));
        assertTrue(result);
    }

    @Test
    void releaseOwnerToBook() {
        when(booksRepositories.findById(1)).thenReturn(Optional.of(book1));

        boolean result = booksService.releaseOwnerToBook(1);

        assertNull(book1.getOwner());
        assertNull(book1.getTakeTime());
        assertTrue(result);
    }

    @Test
    void releaseAllOwnerToBook() {
        when(booksRepositories.findByOwnerId(1)).thenReturn(List.of(book1, book2));

        boolean result = booksService.releaseAllOwnerToBook(1);

        assertNull(book1.getOwner());
        assertNull(book1.getTakeTime());
        assertNull(book2.getOwner());
        assertNull(book2.getTakeTime());
        assertTrue(result);
    }

    @Test
    void testDeleteSuccess() {
        int id = 1;

        boolean result = booksService.delete(id);

        assertTrue(result);
        verify(booksRepositories).deleteById(id);
    }

    @Test
    void testDeleteFailure() {
        int id = 1;
        doThrow(new RuntimeException()).when(booksRepositories).deleteById(id);

        boolean result = booksService.delete(id);

        assertFalse(result);
    }
}