package net.giuse.biblioteca.author;

import net.giuse.biblioteca.book.BookMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = BookMapper.class)
public interface AuthorMapper {
    AuthorDTO toDto(Author entity);
    Author toEntity(AuthorDTO dto);
}