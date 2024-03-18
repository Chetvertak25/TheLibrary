package ru.chetvertak.TheLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.chetvertak.TheLibrary.config.models.Book;

import java.util.List;

@Repository
public interface BooksRepositories extends JpaRepository<Book, Integer> {

    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByOwnerId(int ownerId);
}
