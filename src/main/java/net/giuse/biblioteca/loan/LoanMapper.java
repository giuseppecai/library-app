package net.giuse.biblioteca.loan;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LoanMapper.class)
public interface LoanMapper {
    LoanDTO toDto(Loan entity);
    Loan toEntity(LoanDTO dto);
}