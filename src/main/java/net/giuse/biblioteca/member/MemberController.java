package net.giuse.biblioteca.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.giuse.biblioteca.book.BookDTO;
import net.giuse.biblioteca.book.BookService;
import net.giuse.biblioteca.core.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Member", description = "Member controller")
public class MemberController {
    private final MemberService memberService;
    private final BookService bookService;

    public MemberController(MemberService memberService, BookService bookService) {
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @Operation(summary = "Get all members or get member by name.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of members"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/")
    public ResponseEntity<ResponseWrapper<List<MemberDTO>>> getAllMembers(
            @RequestParam(required = false) String memberName
    ) {
        /* Creo la lista e il messaggio da restituire nel caso in cui la lista è vuota.
         *  Dato che ci sono anche delle ricerche con params, ho deciso di creare dei
         *  messaggi significativi in base al params utilizzato. */
        List<MemberDTO> lists;
        String emptyListMessage = "No members found.";

        if (memberName != null) {
            lists = memberService.findMembersByName(memberName);
            emptyListMessage = "No members found with name '" + memberName + "'.";
        } else {
            lists = memberService.getAllMembers();
        }

        if (lists.isEmpty()) {
            return ResponseEntity
                    .ok(ResponseWrapper.success(emptyListMessage, lists));
        } else {
            return ResponseEntity
                    .ok(ResponseWrapper.success("Members retrieved successfully.", lists));
        }
    }


    @Operation(summary = "Find member with id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<MemberDTO>> getBook(@PathVariable Long id) {
        MemberDTO member = memberService.getMemberById(id);
        return ResponseEntity
                .ok(ResponseWrapper.success("Member retrieved successfully", member));
    }



    @Operation(summary = "Create member.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Member successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict while updating the member"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/")
    public ResponseEntity<ResponseWrapper<MemberDTO>> createBook(@RequestBody @Valid MemberDTO memberDTO) {
        MemberDTO created = memberService.createMember(memberDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseWrapper.success("Member successfully created", created));
    }



    @Operation(summary = "Update member.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "Conflict while updating the book"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<BookDTO>> updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO bookDto) {
        BookDTO updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity
                .ok(ResponseWrapper.success("Book with id " + id + " successfully updated", updatedBook));
    }



    @Operation(summary = "Delete book.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully, no content"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity
                .ok(ResponseWrapper.success("Book with id " + id + " deleted successfully", null));
    }



    @Operation(summary = "Verify if book is available.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book verified successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/available")
    public ResponseEntity<ResponseWrapper<Boolean>> isAvailable(@PathVariable Long id) {
        Boolean isAvailable = bookService.isAvailable(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseWrapper.success("Book verified successfully", isAvailable));
    }
}
