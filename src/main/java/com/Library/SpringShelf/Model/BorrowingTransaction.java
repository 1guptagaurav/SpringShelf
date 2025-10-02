package com.Library.SpringShelf.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrowing_transactions")
@Data
public class BorrowingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User borrower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @Column(nullable = false)
    private LocalDateTime borrowDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(columnDefinition = "DECIMAL(5, 2) DEFAULT 0.00") // e.g., 999.99
    private Double lateFee = 0.0;


    private LocalDate returnDate; // Null until the book is returned

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;
}