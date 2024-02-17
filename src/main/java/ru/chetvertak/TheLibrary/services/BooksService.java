package ru.chetvertak.TheLibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chetvertak.TheLibrary.models.Book;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.repositories.BooksRepositories;
import ru.chetvertak.TheLibrary.repositories.PeopleRepositories;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepositories booksRepositories;
    private final PeopleRepositories peopleRepositories;

    @Autowired
    public BooksService(BooksRepositories booksRepositories, PeopleRepositories peopleRepositories) {
        this.booksRepositories = booksRepositories;
        this.peopleRepositories = peopleRepositories;
    }

    public List<Book> findBooksById(int id) {
        Optional<Person> person = peopleRepositories.findById(id);
        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            List<Book> books = person.get().getBooks();
            for (int i = 0; i < books.size(); i++){
                if (books.get(i).getTakeTime().isBefore(LocalDateTime.now().minusDays(7))){
                    books.get(i).setOverdue(true);
                }
            }
            return books;
        } else {
            return Collections.emptyList();
        }
    }

    public Book findOne(int id) {
        Optional<Book> optional = booksRepositories.findById(id);

        return optional.orElse(null);
    }

    public List<Book> getAllBooks(int page, int booksPerPage, boolean sort) {

        List<Book> books = booksRepositories.findAll();
        if (sort) {
            books.sort(Book.COMPARE_BY_COUNT);
        }
        if (booksPerPage != 0) {
            int startIndex = page * booksPerPage;
            int endIndex = Math.min(startIndex + booksPerPage, books.size());

            if (startIndex >= endIndex) {
                return new ArrayList<>(); // Пустой список, если страница не содержит книг
            }
            books = books.subList(startIndex, endIndex);

            return books;
        }

        return books;
    }

    public List<Book> searchByTitle(String request) {
        return booksRepositories.findByTitleContainingIgnoreCase(request);
    }

    @Transactional
    public void save(Book person) {
        booksRepositories.save(person);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setId(id);
        booksRepositories.save(updateBook);
    }

    @Transactional
    public void assignOwnerToBook(int id, Person person) {
        Optional<Book> book = booksRepositories.findById(id);
        book.get().setOwner(person);
        book.get().setTakeTime(LocalDateTime.now());
        booksRepositories.save(book.get());
    }

    @Transactional
    public void releaseOwnerToBook(int bookId) {
        Book book = booksRepositories.findById(bookId).orElse(null);
        book.setOwner(null);
        book.setTakeTime(null);
        booksRepositories.save(book);
    }


    @Transactional
    public void releaseAllOwnerToBook(int ownerId) {
        List<Book> books = booksRepositories.findByOwnerId(ownerId);
        for (Book book: books) {
            book.setOwner(null);
            book.setTakeTime(null);
            booksRepositories.save(book);
        }
    }




    @Transactional
    public void delete(int id) {
        booksRepositories.deleteById(id);
    }
}