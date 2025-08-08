package net.giuse.biblioteca.author;

import net.giuse.biblioteca.author.exception.AuthorNotFoundException;
import net.giuse.biblioteca.author.exception.BookNotAssociatedException;
import net.giuse.biblioteca.author.exception.InvalidDateException;
import net.giuse.biblioteca.book.*;
import net.giuse.biblioteca.book.exception.BookConflictException;
import net.giuse.biblioteca.book.exception.BookNotFoundException;
import net.giuse.biblioteca.book.exception.InvalidBookDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService{
    private AuthorRepository authorRepository;
    private AuthorMapper authorMapper;
    private BookService bookService;
    private BookMapper bookMapper;
    private BookRepository bookRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper, BookService bookService, BookMapper bookMapper, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public List<AuthorDTO> findAuthorsByName(String name) {
        return authorRepository.findByName(name)
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found."));

        return authorMapper.toDto(author);
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDto) {
        if (authorDto.getBirthDate().isAfter(LocalDate.now()))
            throw new InvalidDateException("Birth date cannot be in the future");

        Author author = authorMapper.toEntity(authorDto);
        author.setId(null);

        if (authorDto.getBooks() != null) {
            List<Book> managedBooks = authorDto.getBooks().stream()
                    .map(bookDto -> {
                        Book book;
                        if (bookDto.getId() != null && bookDto.getId() != 0) {
                            book = bookMapper.toEntity(bookService.getBookById(bookDto.getId()));
                        } else {
                            book = bookMapper.toEntity(bookService.createBook(bookDto));
                        }
                        book.setAuthor(author);
                        return book;
                    })
                    .toList();

            author.setBooks(managedBooks);
        }

        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toDto(savedAuthor);
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDto) {
        if (authorDto.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidDateException("Birth date cannot be in the future");
        }

        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));

        if (authorDto.getBooks() != null) {
            List<Book> managedBooks = authorDto.getBooks().stream()
                    .map(bookDto -> {
                        Book book;
                        if (bookDto.getId() != null && bookDto.getId() != 0) {
                            book = bookMapper.toEntity(bookService.getBookById(bookDto.getId()));
                        } else {
                            book = bookMapper.toEntity(bookService.createBook(bookDto));
                        }
                        book.setAuthor(existingAuthor);
                        return book;
                    })
                    .toList();

            existingAuthor.setBooks(managedBooks);
        }

        existingAuthor.setName(authorDto.getName());
        existingAuthor.setSurname(authorDto.getSurname());
        existingAuthor.setBirthDate(authorDto.getBirthDate());

        return authorMapper.toDto(authorRepository.save(existingAuthor));
    }

    @Transactional
    @Override
    public AuthorDTO addBookToAuthor(Long authorId, Long bookId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + authorId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + bookId + " not found"));

        author.addBook(book);
        book.setAuthor(author);

        return authorMapper.toDto(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + id + " not found"));

        authorRepository.delete(existingAuthor);
    }

    @Override
    public void removeBookFromAuthor(Long authorId, Long bookId) {
        Author author = authorRepository.findById(authorId).orElseThrow(() -> new AuthorNotFoundException("Author not found with id " + authorId));
        Book removedBook = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId));

        boolean isAssociated = author.getBooks()
                .stream()
                .anyMatch(book -> book.getId().equals(bookId));

        if (isAssociated) {
            author.getBooks().removeIf(book -> book.getId().equals(bookId));
            removedBook.setAuthor(null);
            authorRepository.save(author);
            bookRepository.save(removedBook);
        } else
            throw new BookNotAssociatedException("Book with ID " + bookId + " not associated to author with id " + authorId);
    }

    @Override
    public List<AuthorDTO> findByBirthDate(LocalDate birthDate) {
        return authorRepository.findByBirthDate(birthDate)
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }
}
