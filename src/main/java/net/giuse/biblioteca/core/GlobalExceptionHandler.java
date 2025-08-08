package net.giuse.biblioteca.core;

import net.giuse.biblioteca.author.exception.AuthorNotFoundException;
import net.giuse.biblioteca.author.exception.BookNotAssociatedException;
import net.giuse.biblioteca.book.exception.BookConflictException;
import net.giuse.biblioteca.book.exception.BookNotFoundException;
import net.giuse.biblioteca.book.exception.InvalidBookDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * BOOK EXCEPTION
     */

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseWrapper<?>> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseWrapper.failure(ex.getMessage()));
    }

    @ExceptionHandler(InvalidBookDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseWrapper<?>> handleInvalidBookData(InvalidBookDataException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.failure(ex.getMessage()));
    }

    @ExceptionHandler(BookConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseWrapper<?>> handleBookConflict(BookConflictException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ResponseWrapper.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<?>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseWrapper.failure("Internal server error"));
    }


    /*
     * AUTHOR EXCEPTION
     */

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseWrapper<?>> handleAuthorNotFound(AuthorNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseWrapper.failure(ex.getMessage()));
    }

    @ExceptionHandler(BookNotAssociatedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseWrapper<?>> handleBookNotAssociated(BookNotAssociatedException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ResponseWrapper.failure(ex.getMessage()));
    }


    /*
     * GENERIC EXCEPTION
     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseWrapper<?>> handleInvalidFormat(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof DateTimeParseException) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseWrapper.failure("Date format is invalid. Expected format: yyyy-MM-dd"));
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.failure("Malformed request body"));
    }

    /*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exception(Exception e, HttpServletRequest request) {
        String lang = request.getParameter("lang");
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseWrapper.failure("["+ e.getClass().getSimpleName() +"] "+ e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        if (ex instanceof org.springframework.web.servlet.resource.NoResourceFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Resource not found");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred");
    }
    */
}