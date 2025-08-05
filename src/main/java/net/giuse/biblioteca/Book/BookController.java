package net.giuse.biblioteca.Book;

import net.giuse.biblioteca.Author.AuthorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/")
    public ResponseEntity<List<BookDTO>> getAllBooks(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean available
    ) {
        if(authorId != null) return ResponseEntity.ok(bookService.findByAuthor(authorId));
        else if(available != null) return ResponseEntity.ok(bookService.findAvailableBooks());
        else if(title != null)  return ResponseEntity.ok(bookService.searchByTitle(title));

        return ResponseEntity.ok(bookService.getAllBooks());

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/")
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDto) {
        return ResponseEntity.ok(bookService.createBook(bookDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/available")
    public ResponseEntity<Boolean> isAvailable(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.isAvailable(id));
    }
}
