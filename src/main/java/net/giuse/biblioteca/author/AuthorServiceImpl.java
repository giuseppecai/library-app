package net.giuse.biblioteca.author;

import net.giuse.biblioteca.book.Book;
import net.giuse.biblioteca.book.BookMapper;
import net.giuse.biblioteca.book.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{
    private AuthorRepository authorRepository;
    private AuthorMapper authorMapper;
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper, BookRepository bookRepository, BookMapper bookMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public AuthorDTO createAuthor(AuthorDTO authorDto) {
        authorDto.setId(null);
        return authorMapper.toDto(authorRepository.save(authorMapper.toEntity(authorDto)));
    }

    @Override
    public AuthorDTO getAuthorById(Long id) {
        return authorMapper.toDto(authorRepository.findById(id).orElse(null));
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDto) {

        return null;
    }

    @Override
    public void deleteAuthor(Long id) {
        if(!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found with id " + id);
        }
        Author a = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + id));
        for (Book book : a.getBooks()) {
            book.setAuthor(null);
            bookRepository.save(book);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public List<AuthorDTO> searchAuthorsByName(String name) {
        return authorRepository.findByName(name)
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Override
    public List<AuthorDTO> findByBirthDate(LocalDate birthDate) {
        return authorRepository.findByBirthDate(birthDate)
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public void addBookToAuthor(Long authorId, Long bookId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found with id " + authorId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));

        author.addBook(book);
        book.setAuthor(author);

        authorRepository.save(author);
        bookRepository.save(book);
    }

    @Override
    public void removeBookFromAuthor(Long authorId, Long bookId) {
        if(!bookRepository.existsById(bookId)) {
            throw new RuntimeException("Book not found with id " + bookId);
        }
        if(!authorRepository.existsById(authorId)) {
            throw new RuntimeException("Author not found with id " + authorId);
        }

        authorRepository.findById(authorId).orElse(null).removeBook(bookRepository.findById(bookId).orElse(null));
    }
}
