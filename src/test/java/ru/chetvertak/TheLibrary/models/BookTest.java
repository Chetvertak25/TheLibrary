package ru.chetvertak.TheLibrary.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class BookTest {

    @Test
    public void testBookConstructor() {
        Book book = new Book("Test Title", "Test Author", 2022);
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals(2022, book.getYear());
    }

    @Test
    public void testSetAndGetTakeTime() {
        Book book = new Book();
        LocalDateTime takeTime = LocalDateTime.now();
        book.setTakeTime(takeTime);
        assertEquals(takeTime, book.getTakeTime());
    }

    @Test
    public void testSetAndGetOwner() {
        Book book = new Book();
        Person owner = mock(Person.class);
        book.setOwner(owner);
        assertEquals(owner, book.getOwner());
    }

    @Test
    public void testSetAndGetOverdue() {
        Book book = new Book();
        book.setOverdue(true);
        assertTrue(book.isOverdue());
    }
}