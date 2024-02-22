package ru.chetvertak.TheLibrary.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.chetvertak.TheLibrary.models.Book;
import ru.chetvertak.TheLibrary.models.BookComparator;
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
            books.sort(new BookComparator());
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
    public boolean save(Book person) {
        try {
            booksRepositories.save(person);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Transactional
    public boolean update(int id, Book updateBook) {
        try {
            updateBook.setId(id);
            booksRepositories.save(updateBook);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean assignOwnerToBook(int id, Person person) {
        try {
            Optional<Book> book = booksRepositories.findById(id);
            book.get().setOwner(person);
            book.get().setTakeTime(LocalDateTime.now());
            booksRepositories.save(book.get());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean releaseOwnerToBook(int bookId) {
        try {
            Book book = booksRepositories.findById(bookId).orElse(null);
            book.setOwner(null);
            book.setTakeTime(null);
            booksRepositories.save(book);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean releaseAllOwnerToBook(int ownerId) {
        try {
            List<Book> books = booksRepositories.findByOwnerId(ownerId);
            for (Book book: books) {
                book.setOwner(null);
                book.setTakeTime(null);
                booksRepositories.save(book);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    @Transactional
    public boolean delete(int id) {
        try {
            booksRepositories.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}