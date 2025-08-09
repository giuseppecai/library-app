package net.giuse.biblioteca.book;

import net.giuse.biblioteca.author.Author;
import net.giuse.biblioteca.author.AuthorMapper;
import net.giuse.biblioteca.author.AuthorRepository;
import net.giuse.biblioteca.author.exception.AuthorNotFoundException;
import net.giuse.biblioteca.book.exception.BookConflictException;
import net.giuse.biblioteca.book.exception.BookNotFoundException;
import net.giuse.biblioteca.book.exception.InvalidBookDataException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto) // book -> bookMapper.toDto(book)
                .toList();
    }

    @Override
    public List<BookDTO> searchByTitle(String title) {
        return bookRepository.findByTitle(title)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDTO> findAvailableBooks() {
        return bookRepository.findByAvailableTrue()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDTO> findByAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author with ID " + authorId + " not found"));

        return bookRepository.findByAuthor(author)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));

        return bookMapper.toDto(book);
    }

    @Override
    public BookDTO createBook(BookDTO bookDto) {
        bookDto.setId(null);
        if (bookDto.getIsbn() == null || bookDto.getIsbn().isBlank())
            throw new InvalidBookDataException("Isbn cannot be empty");
        if (!bookRepository.findByIsbn(bookDto.getIsbn()).isEmpty())
            throw new BookConflictException("A book with the same ISBN already exists");
        return bookMapper.toDto(bookRepository.save(bookMapper.toEntity(bookDto)));
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDto) {
        /* cerco il libro con l'id fornito nei params, se non lo trovo eccezione BookNotFound */
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));

        /* verifico che l'isbn non sia vuoto e verifico
         * che non ci siano altri libri con lo stesso isbn (voglio che sia univoco) */
        if (bookDto.getIsbn() == null || bookDto.getIsbn().isBlank())
            throw new InvalidBookDataException("Isbn cannot be empty");
        if (!bookRepository.findByIsbn(bookDto.getIsbn()).isEmpty())
            throw new BookConflictException("A book with the same ISBN already exists");

        /* se i controlli passano, aggiorno i campi dell'oggetto esistente */
        existingBook.setTitle(bookDto.getTitle());
        existingBook.setIsbn(bookDto.getIsbn());
        existingBook.setPublicationDate(bookDto.getPublicationDate());
        existingBook.setAvailable(bookDto.getAvailable());

        /* se nel body ho messo un autore, voglio aggiungerlo al libro esistente
         * altrimenti imposto l'autore a null (non lo faccio nel primo if perché
         * findById con null può dare errori) */
        if (bookDto.getAuthor() != null) {
            Author a = authorRepository.findById(bookDto.getAuthor()).orElse(null);
            existingBook.setAuthor(a);
        } else {
            existingBook.setAuthor(null);
        }

        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public void deleteBook(Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));

        bookRepository.delete(existingBook);
    }

    @Override
    public boolean isAvailable(Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));

        return existingBook.getAvailable();
    }




    @Override
    public void markAsLoaned(Long bookId) {
        Book b = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));
        if(b.getAvailable()) b.setAvailable(false);
    }

    @Override
    public void markAsReturned(Long bookId) {
        Book b = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id " + bookId));
        if(!b.getAvailable()) b.setAvailable(true);
    }
}
