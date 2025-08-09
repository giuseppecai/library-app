package net.giuse.biblioteca.loan;

import net.giuse.biblioteca.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    int countByMemberIdAndStatusIn(Long memberId, List<LoanStatus> statuses);
}
