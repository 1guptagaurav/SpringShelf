package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.Aop.Auditable;
import com.Library.SpringShelf.Dto.BorrowRequestDto;
import com.Library.SpringShelf.Dto.ReturnRequestDto;
import com.Library.SpringShelf.Dto.TransactionDto;
import com.Library.SpringShelf.Model.*;
import com.Library.SpringShelf.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BorrowingTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BookCopyRepository bookCopyRepository;
    private static final int MAX_BORROWED_BOOKS = 5;
    private static final double LATE_FEE_PER_DAY = 10.0;

    @Transactional
    @Auditable(action = "BOOK_BORROWED")
    public TransactionDto borrowBook(BorrowRequestDto borrowRequest,String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        BookCopy bookCopy = bookCopyRepository.findById(borrowRequest.getBookCopyId())
                .orElseThrow(() -> new RuntimeException("Book copy not found"));

        if (!bookCopy.getBook().isBorrowable()) {
            throw new RuntimeException("This book is for reference only and cannot be borrowed.");
        }

        if (bookCopy.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new RuntimeException("Book copy is not available for borrowing.");
        }

        int activeBorrows = transactionRepository.countByBorrowerAndStatus(user, TransactionStatus.ACTIVE);
        if (activeBorrows >= MAX_BORROWED_BOOKS) {
            throw new RuntimeException("User has reached the maximum borrowing limit of " + MAX_BORROWED_BOOKS + " books.");
        }


        bookCopy.setAvailabilityStatus(AvailabilityStatus.BORROWED);
        bookCopyRepository.save(bookCopy);

        BorrowingTransaction transaction = new BorrowingTransaction();
        transaction.setBorrower(user);
        transaction.setBookCopy(bookCopy);
        transaction.setBorrowDate(LocalDateTime.now());
        transaction.setDueDate(LocalDate.now().plusDays(14));
        transaction.setStatus(TransactionStatus.ACTIVE);

        BorrowingTransaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    @Transactional
    @Auditable(action = "BOOK_RETURNED")
    public TransactionDto returnBook(ReturnRequestDto returnRequest, String username) {
        BookCopy bookCopy = bookCopyRepository.findById(returnRequest.getBookCopyId())
                .orElseThrow(() -> new RuntimeException("Book copy not found"));


        BorrowingTransaction transaction = transactionRepository.findByBookCopyAndStatus(bookCopy, TransactionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active borrowing record found for this book copy."));

        boolean isBorrower = transaction.getBorrower().getEmail().equals(username);
        boolean isStaff = transaction.getBorrower().getRoles().stream()
                .anyMatch(role -> role.getRole() == Rolename.ADMIN || role.getRole() == Rolename.LIBRARIAN);

        if (!isBorrower && !isStaff) {
            throw new RuntimeException("You are not authorized to return this book.");
        }

        bookCopy.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        bookCopyRepository.save(bookCopy);

        transaction.setReturnDate(LocalDate.now());
        if (LocalDate.now().isAfter(transaction.getDueDate())) {
            transaction.setStatus(TransactionStatus.OVERDUE);

            long daysOverdue = ChronoUnit.DAYS.between(transaction.getDueDate(), transaction.getReturnDate());


            double fee = daysOverdue * LATE_FEE_PER_DAY;
            transaction.setLateFee(fee);
        } else {
            transaction.setStatus(TransactionStatus.RETURNED);
            transaction.setLateFee(0.0);
        }

        BorrowingTransaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }


    private TransactionDto convertToDto(BorrowingTransaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setUserId(transaction.getBorrower().getId());
        dto.setUserEmail(transaction.getBorrower().getEmail());
        dto.setBookCopyId(transaction.getBookCopy().getId());
        dto.setBookTitle(transaction.getBookCopy().getBook().getTitle());
        dto.setBorrowDate(transaction.getBorrowDate());
        dto.setDueDate(transaction.getDueDate());
        dto.setReturnDate(transaction.getReturnDate());
        dto.setLateFee(transaction.getLateFee());
        dto.setStatus(transaction.getStatus());
        return dto;
    }
}
