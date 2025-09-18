package net.giuse.biblioteca.loan;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import net.giuse.biblioteca.book.BookDTO;
import net.giuse.biblioteca.member.MemberDTO;
import net.giuse.biblioteca.model.LoanStatus;

import java.time.LocalDate;
import java.util.Objects;

public class LoanDTO {
    private Long id;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private Long memberId;
    private Long bookId;

    public LoanDTO() {
    }

    public LoanDTO(Long id, LocalDate loanDate, LocalDate returnDate, LoanStatus status, Long memberId, Long bookId) {
        this.id = id;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
        this.memberId = memberId;
        this.bookId = bookId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoanDTO loanDTO = (LoanDTO) o;
        return Objects.equals(id, loanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
