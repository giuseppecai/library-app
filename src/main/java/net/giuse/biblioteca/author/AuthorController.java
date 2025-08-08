package net.giuse.biblioteca.author;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.giuse.biblioteca.book.BookDTO;
import net.giuse.biblioteca.book.BookService;
import net.giuse.biblioteca.core.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@Tag(name = "Author", description = "Author controller")
public class AuthorController {
    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Operation(summary = "Get all authors or get authors by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of authors")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseWrapper<List<AuthorDTO>>> getAllAuthors(
            @RequestParam(required = false) String name) {
        List<AuthorDTO> lists;
        String emptyListMessage = "No authors found.";

        if (name != null) {
            lists = authorService.findAuthorsByName(name);
            emptyListMessage = "No authors with " + name + " found.";
        }
        else
            lists = authorService.getAllAuthors();

        if(lists.isEmpty())
            return ResponseEntity.ok(ResponseWrapper.success(emptyListMessage, lists));
        else
            return ResponseEntity.ok(ResponseWrapper.success("Authors retrieved successfully.", lists));
    }

    @Operation(summary = "Find author with id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<AuthorDTO>> getAuthor(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        return ResponseEntity
                .ok(ResponseWrapper.success("Author with id " + id + " successfully found.", author));
    }

    @Operation(summary = "Find all books about one author.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found")
    })
    @GetMapping("/{authorId}/books")
    public ResponseEntity<ResponseWrapper<List<BookDTO>>> getBooksByAuthor(@PathVariable Long authorId) {
        List<BookDTO> books = bookService.findByAuthor(authorId);
        return ResponseEntity
                .ok(ResponseWrapper.success("Books about author with id " + authorId, books));
    }

    @Operation(summary = "Create author. For create new books set book id = 0 or book id = null")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<AuthorDTO>> createAuthor(@RequestBody AuthorDTO authorDto) {
        AuthorDTO created = authorService.createAuthor(authorDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseWrapper.success("Author successfully created", created));
    }

    @Operation(summary = "Update author. For create new books set book id = 0 or book id = null")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<AuthorDTO>> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDto) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDto);
        return ResponseEntity
                .ok(ResponseWrapper.success("Author with id " + id + " successfully updated", updatedAuthor));
    }

    @Operation(summary = "Add existing book to author.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "404", description = "Book or author not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{author_id}/{book_id}")
    public ResponseEntity<ResponseWrapper<AuthorDTO>> addBookToAuthor(@PathVariable Long author_id, @PathVariable Long book_id) {
        AuthorDTO updatedAuthor = authorService.addBookToAuthor(author_id, book_id);
        return ResponseEntity
                .ok(ResponseWrapper.success("Book with id " + book_id + " added to author with id " + author_id + " successfly", updatedAuthor));
    }

    @Operation(summary = "Delete author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted successfully, no content"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity
                .ok(ResponseWrapper.success("Book with id " + id + " removed successfully", null));
    }

    @Operation(summary = "Remove book from author.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book removed successfully, no content"),
            @ApiResponse(responseCode = "404", description = "Book or Author not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{authorId}/{bookId}")
    public ResponseEntity<ResponseWrapper<Void>> removeBookFromAuthor(@PathVariable Long authorId, @PathVariable Long bookId) {
        authorService.removeBookFromAuthor(authorId, bookId);
        return ResponseEntity
                .ok(ResponseWrapper.success("Book with id " + bookId + " removed successfully from Author with id " + authorId, null));
    }
}

