package net.giuse.biblioteca.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByName(String name);
    List<Author> findByBirthDate(LocalDate birthDate);
}
