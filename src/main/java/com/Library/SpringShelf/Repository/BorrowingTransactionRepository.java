package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Model.BookCopy;
import com.Library.SpringShelf.Model.BorrowingTransaction;
import com.Library.SpringShelf.Model.TransactionStatus;
import com.Library.SpringShelf.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {

    // Find an ongoing transaction for a specific book copy
    Optional<BorrowingTransaction> findByBookCopyAndStatus(BookCopy bookCopy, TransactionStatus status);

    // Count how many books a user currently has borrowed
    int countByBorrowerAndStatus(User user, TransactionStatus status);
}
