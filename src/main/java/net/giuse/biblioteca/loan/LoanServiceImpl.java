package net.giuse.biblioteca.loan;

import net.giuse.biblioteca.book.Book;
import net.giuse.biblioteca.book.BookRepository;
import net.giuse.biblioteca.book.exception.*;
import net.giuse.biblioteca.core.LibraryConfig;
import net.giuse.biblioteca.loan.exception.*;
import net.giuse.biblioteca.member.Member;
import net.giuse.biblioteca.member.MemberRepository;
import net.giuse.biblioteca.member.exception.*;
import net.giuse.biblioteca.model.LoanStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LibraryConfig libraryConfig;

    public LoanServiceImpl(LoanRepository loanRepository, LoanMapper loanMapper, BookRepository bookRepository, MemberRepository memberRepository, LibraryConfig libraryConfig) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.libraryConfig = libraryConfig;
    }


    @Override
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(loanMapper::toDto)
                .toList();
    }

    @Override
    public LoanDTO getLoanById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan with id " + id + " not found"));

        return loanMapper.toDto(loan);
    }

    @Override
    public LoanDTO createLoan(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + memberId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not found"));

        if(!book.getAvailable())
            throw new BookConflictException("Book with id " + book.getId() + " not available");

        int activeLoans = loanRepository.countByMemberIdAndStatusIn(memberId, List.of(LoanStatus.REQUESTED, LoanStatus.ACTIVE));
        if (activeLoans >= libraryConfig.getMaxLoansAllowed()) {
            throw new LoanLimitExceededException("Member has reached the max number of active loans (" + libraryConfig.getMaxLoansAllowed() + ")");
        }

        Loan loan = new Loan(member, book, LoanStatus.REQUESTED);
        member.addLoan(loan);
        book.addLoan(loan);
        loanRepository.save(loan);
        memberRepository.save(member);
        bookRepository.save(book);
        return loanMapper.toDto(loan);
    }



    @Override
    public void deleteLoan(Long id) {
        Loan existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan with id " + id + " not found"));
        loanRepository.delete(existingLoan);
    }

    @Override
    public void requestLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan with id " + id + " not found"));
        loan.setStatus(LoanStatus.REQUESTED);
        loanRepository.save(loan);
    }

    @Override
    public void cancelLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan with id " + id + " not found"));
        if(loan.getStatus() != LoanStatus.REQUESTED)
            throw new LoanIllegalStateException("Loan can be cancelled only if in requested state");
        loan.setStatus(LoanStatus.CANCELLED);
        loanRepository.save(loan);
    }

    @Override
    public void activateLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan with id " + id + " not found"));
        if(loan.getStatus() != LoanStatus.REQUESTED)
            throw new LoanIllegalStateException("Loan can be activated only if in requested state");
        loan.setStatus(LoanStatus.ACTIVE);
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setAvailable(false);
        bookRepository.save(book);
    }

    @Override
    public void rejectLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan with id " + id + " not found"));
        if(loan.getStatus() != LoanStatus.REQUESTED)
            throw new LoanIllegalStateException("Loan can be rejected only if in requested state");
        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
    }

    @Override
    public void returnLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan with id " + id + " not found"));
        if(loan.getStatus() != LoanStatus.ACTIVE)
            throw new LoanIllegalStateException("Loan can be returned only if in active state");
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
    }
}
