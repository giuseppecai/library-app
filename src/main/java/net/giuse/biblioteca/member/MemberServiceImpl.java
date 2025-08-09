package net.giuse.biblioteca.member;

import net.giuse.biblioteca.book.Book;
import net.giuse.biblioteca.book.BookMapper;
import net.giuse.biblioteca.book.BookRepository;
import net.giuse.biblioteca.book.BookService;
import net.giuse.biblioteca.book.exception.BookNotFoundException;
import net.giuse.biblioteca.loan.Loan;
import net.giuse.biblioteca.loan.LoanDTO;
import net.giuse.biblioteca.loan.LoanMapper;
import net.giuse.biblioteca.loan.LoanService;
import net.giuse.biblioteca.member.exception.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final BookRepository bookRepository;
    private final LoanService loanService;
    private final BookService bookService;
    private final LoanMapper loanMapper;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper, BookRepository bookRepository, LoanService loanService, BookService bookService, LoanMapper loanMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.bookRepository = bookRepository;
        this.loanService = loanService;
        this.bookService = bookService;
        this.loanMapper = loanMapper;
    }

    @Override
    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(memberMapper::toDto)
                .toList();
    }

    @Override
    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + id + " not found"));
        return memberMapper.toDto(member);
    }

    @Override
    public MemberDTO createMember(MemberDTO memberDTO) {
        memberDTO.setId(null);
        if(memberRepository.findByEmail(memberDTO.getEmail()).isPresent())
            throw new MemberConflictException("Member with email " + memberDTO.getEmail() + " already exists");

        Member member = memberMapper.toEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return memberMapper.toDto(savedMember);
    }

    @Override
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        memberDTO.setId(null);
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + id + " not found"));

        existingMember.setName(memberDTO.getName());
        existingMember.setSurname(memberDTO.getSurname());
        String oldEmail = existingMember.getEmail();
        if(memberRepository.findByEmail(memberDTO.getEmail()).isPresent() && !memberDTO.getEmail().equals(oldEmail))
            throw new MemberConflictException("Member with email " + memberDTO.getEmail() + " already exists");
        existingMember.setEmail(memberDTO.getEmail());
        existingMember.setPhoneNumber(memberDTO.getPhoneNumber());
        existingMember.setActive(memberDTO.getActive());

        memberRepository.save(existingMember);
        return memberMapper.toDto(existingMember);
    }

    @Override
    public void deleteMember(Long id) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + id + " not found"));
        memberRepository.delete(existingMember);
    }

    @Override
    public List<MemberDTO> findMembersByName(String name) {
        return memberRepository.findByName(name)
                .stream()
                .map(memberMapper::toDto)
                .toList();
    }

    // richiedi prestito libro
    @Override
    public LoanDTO borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member with id " + memberId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book with id " + bookId + " not found"));

        Loan loan = loanService.createLoan(memberId, bookId);

        return loanMapper.toDto(loan);
    }

    @Override
    public void cancelLoan(Long loanId) {
        loanService.cancelLoan(loanId);
    }

    @Override
    public void returnLoan(Long loanId) {
        Loan loan = loanService.getLoanById(loanId);
        bookService.markAsReturned(loan.getBook().getId());
        loanService.returnLoan(loanId);
    }
}
