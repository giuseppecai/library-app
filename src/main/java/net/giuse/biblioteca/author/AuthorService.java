package net.giuse.biblioteca.author;

import java.time.LocalDate;
import java.util.List;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDto);
    AuthorDTO getAuthorById(Long id);
    List<AuthorDTO> getAllAuthors();
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDto);
    void deleteAuthor(Long id);

    List<AuthorDTO> findAuthorsByName(String name);
    List<AuthorDTO> findByBirthDate(LocalDate birthDate);

    AuthorDTO addBookToAuthor(Long authorId, Long bookId);
    void removeBookFromAuthor(Long authorId, Long bookId);
}
