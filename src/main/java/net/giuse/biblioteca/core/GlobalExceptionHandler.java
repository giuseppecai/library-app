package net.giuse.biblioteca.core;

import net.giuse.biblioteca.book.exception.BookConflictException;
import net.giuse.biblioteca.book.exception.BookNotFoundException;
import net.giuse.biblioteca.book.exception.InvalidBookDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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