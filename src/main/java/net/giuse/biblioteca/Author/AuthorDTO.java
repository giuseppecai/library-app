package net.giuse.biblioteca.Author;

import net.giuse.biblioteca.Book.BookDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class AuthorDTO {
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private List<BookDTO> books;

    public AuthorDTO() {

    }

    public AuthorDTO(Long id, String name, String surname, LocalDate birthDate, List<BookDTO> books) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDTO authorDTO = (AuthorDTO) o;
        return Objects.equals(id, authorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, birthDate, books);
    }
}
