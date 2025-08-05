package net.giuse.biblioteca.Author;

import net.giuse.biblioteca.Book.BookMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface AuthorMapper {
    AuthorDTO toDto(Author entity);
    Author toEntity(AuthorDTO dto);
}