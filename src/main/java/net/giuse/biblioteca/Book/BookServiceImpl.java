package net.giuse.biblioteca.Book;

import net.giuse.biblioteca.Author.Author;
import net.giuse.biblioteca.Author.AuthorMapper;
import net.giuse.biblioteca.Author.AuthorRepository;
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
    public BookDTO createBook(BookDTO bookDto) {
        bookDto.setId(null);
        return bookMapper.toDto(bookRepository.save(bookMapper.toEntity(bookDto)));
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book b = bookRepository.findById(id).orElse(null);
        if(b == null) return null;
        return bookMapper.toDto(b);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                      .stream()
                      .map(bookMapper::toDto) // book -> bookMapper.toDto(book)
                      .toList();
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDto) {
        Book existingBook = bookMapper.toEntity(getBookById(id));
        if(existingBook == null) return null;
        existingBook.setTitle(bookDto.getTitle());
        existingBook.setIsbn(bookDto.getIsbn());
        existingBook.setPublicationDate(bookDto.getPublicationDate());
        existingBook.setAvailable(bookDto.getAvailable());
        Author a = authorRepository.findById(bookDto.getAuthor()).orElseThrow(() -> new RuntimeException("Author not found with id " + bookDto.getAuthor()));
        existingBook.setAuthor(a);
        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
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
                .orElseThrow(() -> new RuntimeException("Author not found with id " + authorId));
        return bookRepository.findByAuthor(author)
                .stream()
                .map(bookMapper::toDto)
                .toList();
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

    @Override
    public boolean isAvailable(Long bookId) {
        BookDTO b = getBookById(bookId);
        if(b==null) return false;
        return b.getAvailable();
    }
}
