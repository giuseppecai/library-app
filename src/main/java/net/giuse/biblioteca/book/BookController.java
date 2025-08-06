package net.giuse.biblioteca.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.giuse.biblioteca.core.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Book", description = "Book controller")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books, get books by author, by title or only available books")
    @ApiResponse(responseCode = "200", description = "List of books")
    @GetMapping("/")
    public ResponseEntity<ResponseWrapper<List<BookDTO>>> getAllBooks(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Boolean available
    ) {
        List<BookDTO> lists;
        String emptyListMessage = "No books found.";

        if(title != null)  {
            lists = bookService.searchByTitle(title);
            emptyListMessage = "No books found with title '" + title + "'.";
        }
        else if(available != null) {
            lists = bookService.findAvailableBooks();
            emptyListMessage = "No books available found.";
        }
        else if(authorId != null) {
            lists = bookService.findByAuthor(authorId);
            emptyListMessage = "No books found with author id '" + authorId + "'.";
        }
        else lists = bookService.getAllBooks();

        if(lists.isEmpty()) return ResponseEntity.ok(ResponseWrapper.success(emptyListMessage, lists));
        else return ResponseEntity.ok(ResponseWrapper.success("Books retrieved successfully.", lists));
    }

    @Operation(summary = "Find book with id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<BookDTO>> getBook(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        if(book != null) return ResponseEntity.ok(ResponseWrapper.success("Book retrieved successfully", bookService.getBookById(id)));
        else return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseWrapper.failure("Book with id " + id + " not found"));
    }

    @Operation(summary = "Create book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<BookDTO>> createBook(@RequestBody @Valid BookDTO bookDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseWrapper.success(bookService.createBook(bookDto)));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseWrapper.failure("Internal server error"));
        }
    }

    @Operation(summary = "Update book")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "Conflict while updating the book"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<BookDTO>> updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO bookDto) {
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookDto);
            if (updatedBook != null) return ResponseEntity.ok(ResponseWrapper.success(updatedBook));
            else return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseWrapper.failure("Book not found"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseWrapper.failure("Unexpected error"));
        }
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
