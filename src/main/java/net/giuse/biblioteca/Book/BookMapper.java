package net.giuse.biblioteca.Book;

import net.giuse.biblioteca.Author.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "author.id", target = "author")
    BookDTO toDto(Book entity);

    @Mapping(source = "author", target = "author")
    Book toEntity(BookDTO dto);

    default Author map(Long authorId) {
        if (authorId == null) {
            return null;
        }
        Author author = new Author();
        author.setId(authorId);
        return author;
    }

    default Long map(Author author) {
        if (author == null) {
            return null;
        }
        return author.getId();
    }
}