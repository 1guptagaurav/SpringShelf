package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.Model.*;
import com.Library.SpringShelf.Repository.BookCopyRepository;
import com.Library.SpringShelf.Repository.BorrowingTransactionRepository;
import com.Library.SpringShelf.Repository.UserRepository;
import com.Library.SpringShelf.Dto.BorrowRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // This enables Mockito for our test class
class TransactionServiceTest {

    @Mock // Creates a "fake" version of this repository
    private BorrowingTransactionRepository transactionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookCopyRepository bookCopyRepository;

    @InjectMocks // Creates a real instance of TransactionService and injects the mocks above
    private TransactionService transactionService;

    @Test
    void borrowBook_shouldThrowException_whenBorrowLimitIsReached() {
        // --- 1. GIVEN (Arrange) ---
        // Create the necessary input data and mock objects
        BorrowRequestDto borrowRequest = new BorrowRequestDto();
        borrowRequest.setBookCopyId(1L);

        String username = "test@user.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(username);

        Book book = new Book();
        book.setBorrowable(true);
        BookCopy bookCopy = new BookCopy();
        bookCopy.setId(1);
        bookCopy.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        bookCopy.setBook(book);

        // Define the behavior of our mocks
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(bookCopy));

        // This is the crucial rule for our test:
        // WHEN the count method is called for this user, THEN return 5 (the max limit)
        when(transactionRepository.countByBorrowerAndStatus(user, TransactionStatus.ACTIVE)).thenReturn(5);

        // --- 2. WHEN & 3. THEN (Act & Assert) ---
        // We expect a RuntimeException to be thrown when we call borrowBook
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.borrowBook(borrowRequest, username);
        });

        // Finally, we assert that the exception message is what we expect
        assertEquals("User has reached the maximum borrowing limit of 5 books.", exception.getMessage());
    }
}