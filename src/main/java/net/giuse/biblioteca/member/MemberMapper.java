package net.giuse.biblioteca.member;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MemberMapper.class)
public interface MemberMapper {
    MemberDTO toDto(Member entity);
    Member toEntity(MemberDTO dto);
}