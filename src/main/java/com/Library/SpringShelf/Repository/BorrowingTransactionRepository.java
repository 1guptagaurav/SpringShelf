package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Dto.MostBorrowedBookDto;
import com.Library.SpringShelf.Model.BookCopy;
import com.Library.SpringShelf.Model.BorrowingTransaction;
import com.Library.SpringShelf.Model.TransactionStatus;
import com.Library.SpringShelf.Model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {


    Optional<BorrowingTransaction> findByBookCopyAndStatus(BookCopy bookCopy, TransactionStatus status);

    void deleteAllByBorrower(User borrower);

    int countByBorrowerAndStatus(User user, TransactionStatus status);

    List<BorrowingTransaction> findByStatusAndDueDateBefore(TransactionStatus status, LocalDate date);

    @Query("SELECT NEW com.Library.SpringShelf.Dto.MostBorrowedBookDto(" +
            "   bc.book.id, " +
            "   bc.book.title, " +
            "   bc.book.author, " +
            "   COUNT(t.id)) " +
            "FROM BorrowingTransaction t " +
            "JOIN t.bookCopy bc " +
            "GROUP BY bc.book.id, bc.book.title, bc.book.author " +
            "ORDER BY COUNT(t.id) DESC")
    List<MostBorrowedBookDto> findMostBorrowedBooks(Pageable pageable);
}
