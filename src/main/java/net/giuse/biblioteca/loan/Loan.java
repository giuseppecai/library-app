package net.giuse.biblioteca.loan;

import jakarta.persistence.*;
import net.giuse.biblioteca.book.Book;
import net.giuse.biblioteca.member.Member;
import net.giuse.biblioteca.model.LoanStatus;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate loanDate;
    private LocalDate returnDate;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public Loan() {

    }

    public Loan(Long id, Member member, Book book, LoanStatus status) {
        this.id = id;
        this.loanDate = LocalDate.now();
        this.returnDate = null;
        this.status = status;
        this.member = member;
        this.book = book;
    }

    public Loan(Member member, Book book, LoanStatus status) {
        this.loanDate = LocalDate.now();
        this.returnDate = null;
        this.status = status;
        this.member = member;
        this.book = book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        if (this.member != member) {
            this.member = member;
            member.addLoan(this);
        }
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        if (this.book != book) {
            this.book = book;
            book.addLoan(this);
        }
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
