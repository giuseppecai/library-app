package net.giuse.biblioteca.Author;

import net.giuse.biblioteca.Book.BookDTO;

import java.time.LocalDate;
import java.util.List;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDto);
    AuthorDTO getAuthorById(Long id);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDto);
    void deleteAuthor(Long id);

    List<AuthorDTO> searchAuthorsByName(String name);
    List<AuthorDTO> findByBirthDate(LocalDate birthDate);

    void addBookToAuthor(Long authorId, Long bookId);
    void removeBookFromAuthor(Long authorId, Long bookId);
}
