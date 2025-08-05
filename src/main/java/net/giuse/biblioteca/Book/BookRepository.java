package net.giuse.biblioteca.Book;

import net.giuse.biblioteca.Author.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
    List<Book> findByAvailableTrue();
    List<Book> findByAuthor(Author author);
}
