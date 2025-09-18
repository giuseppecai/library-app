package net.giuse.biblioteca.loan;

import net.giuse.biblioteca.book.BookDTO;
import net.giuse.biblioteca.member.MemberDTO;

import java.util.List;

public interface LoanService {

    List<LoanDTO> getAllLoans();
    LoanDTO getLoanById(Long id);

    LoanDTO createLoan(Long memberId, Long bookId);
    void deleteLoan(Long id);


    // tutti i metodi per cambiare lo stato
    void requestLoan(Long id);
    void cancelLoan(Long id);
    void returnLoan(Long id);
    void activateLoan(Long id);
    void rejectLoan(Long id);
}
