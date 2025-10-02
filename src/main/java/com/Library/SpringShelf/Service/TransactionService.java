package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.DTO.BorrowRequestDto;
import com.Library.SpringShelf.DTO.ReturnRequestDto;
import com.Library.SpringShelf.DTO.TransactionDto;
import com.Library.SpringShelf.Model.*;
import com.Library.SpringShelf.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BorrowingTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BookCopyRepository bookCopyRepository;
    private static final int MAX_BORROWED_BOOKS = 12;

    @Transactional
    public TransactionDto borrowBook(BorrowRequestDto borrowRequest,String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        BookCopy bookCopy = bookCopyRepository.findById(borrowRequest.getBookCopyId())
                .orElseThrow(() -> new RuntimeException("Book copy not found"));

        // --- Business Rule Validations ---
        // 1. Check if the book is borrowable at all
        if (!bookCopy.getBook().isBorrowable()) {
            throw new RuntimeException("This book is for reference only and cannot be borrowed.");
        }
        // 2. Check if the copy is available
        if (bookCopy.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
            throw new RuntimeException("Book copy is not available for borrowing.");
        }
        // 3. Check if the user has reached their borrowing limit
        int activeBorrows = transactionRepository.countByBorrowerAndStatus(user, TransactionStatus.ACTIVE);
        if (activeBorrows >= MAX_BORROWED_BOOKS) {
            throw new RuntimeException("User has reached the maximum borrowing limit of " + MAX_BORROWED_BOOKS + " books.");
        }

        // --- Create Transaction and Update Status ---
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
    public TransactionDto returnBook(ReturnRequestDto returnRequest, String username) {
        BookCopy bookCopy = bookCopyRepository.findById(returnRequest.getBookCopyId())
                .orElseThrow(() -> new RuntimeException("Book copy not found"));

        // Find the active transaction for this book copy
        BorrowingTransaction transaction = transactionRepository.findByBookCopyAndStatus(bookCopy, TransactionStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active borrowing record found for this book copy."));

        boolean isBorrower = transaction.getBorrower().getEmail().equals(username);
        boolean isStaff = transaction.getBorrower().getRoles().stream()
                .anyMatch(role -> role.getRole() == Rolename.ADMIN || role.getRole() == Rolename.LIBRARIAN);

        if (!isBorrower && !isStaff) {
            throw new RuntimeException("You are not authorized to return this book.");
        }
        // --- Update Transaction and Book Status ---
        bookCopy.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        bookCopyRepository.save(bookCopy);

        transaction.setReturnDate(LocalDate.now());
        // Check if the book is returned late
        if (LocalDate.now().isAfter(transaction.getDueDate())) {
            transaction.setStatus(TransactionStatus.OVERDUE);
            // Future logic for calculating fines can go here
        } else {
            transaction.setStatus(TransactionStatus.RETURNED);
        }

        BorrowingTransaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    // Helper to convert entity to DTO
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
        dto.setStatus(transaction.getStatus());
        return dto;
    }
}
