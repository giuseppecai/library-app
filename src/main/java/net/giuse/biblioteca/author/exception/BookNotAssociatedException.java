package net.giuse.biblioteca.author.exception;

public class BookNotAssociatedException extends RuntimeException {
    public BookNotAssociatedException(String message) {
        super(message);
    }
}
