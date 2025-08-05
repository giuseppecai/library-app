package net.giuse.biblioteca.Book;

import java.util.List;

public interface BookService {
    BookDTO createBook(BookDTO bookDto);
    BookDTO getBookById(Long id);
    List<BookDTO> getAllBooks();
    BookDTO updateBook(Long id, BookDTO bookDto);
    void deleteBook(Long id);

    List<BookDTO> searchByTitle(String title);
    List<BookDTO> findAvailableBooks();
    List<BookDTO> findByAuthor(Long authorId);

    void markAsLoaned(Long bookId);
    void markAsReturned(Long bookId);
    boolean isAvailable(Long bookId);
}