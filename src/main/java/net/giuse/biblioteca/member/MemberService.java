package net.giuse.biblioteca.member;

import net.giuse.biblioteca.loan.LoanDTO;

import java.time.LocalDate;
import java.util.List;

public interface MemberService {
    List<MemberDTO> getAllMembers();
    MemberDTO getMemberById(Long id);

    MemberDTO createMember(MemberDTO memberDTO);
    MemberDTO updateMember(Long id, MemberDTO memberDTO);
    void deleteMember(Long id);

    List<MemberDTO> findMembersByName(String name);

    // richiede un libro in prestito
    LoanDTO borrowBook(Long memberId, Long bookId);
    // annulla una richiesta non ancora presa in carico
    void cancelLoan(Long loanId);
    // restituzione di un prestito già richiesto e accettato
    void returnLoan(Long loanId);
}
