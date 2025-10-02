package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Model.BookCopy;
import com.Library.SpringShelf.Model.BorrowingTransaction;
import com.Library.SpringShelf.Model.TransactionStatus;
import com.Library.SpringShelf.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {


    Optional<BorrowingTransaction> findByBookCopyAndStatus(BookCopy bookCopy, TransactionStatus status);

    void deleteAllByBorrower(User borrower);

    int countByBorrowerAndStatus(User user, TransactionStatus status);

    List<BorrowingTransaction> findByStatusAndDueDateBefore(TransactionStatus status, LocalDate date);

}
