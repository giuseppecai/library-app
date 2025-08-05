package net.giuse.biblioteca.Author;

import net.giuse.biblioteca.Book.BookDTO;
import net.giuse.biblioteca.Book.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @PostMapping("/")
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors(
            @RequestParam(required = false) String name) {
        if (name != null) {
            return ResponseEntity.ok(authorService.searchAuthorsByName(name));
        }
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDto) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDto));
    }

    @PutMapping("/{author_id}/{book_id}")
    public ResponseEntity<Void> addBookToAuthor(@PathVariable Long author_id, @PathVariable Long book_id) {
        authorService.addBookToAuthor(author_id, book_id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{author_id}/{book_id}")
    public ResponseEntity<Void> deleteBookFromAuthor(@PathVariable Long author_id, @PathVariable Long book_id) {
        authorService.removeBookFromAuthor(author_id, book_id);
        return ResponseEntity.ok().build();
    }

    // Extra: ottenere tutti i libri di un autore
    @GetMapping("/{id}/books")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findByAuthor(id));
    }
}

