package com.Library.SpringShelf.DTO;

import com.Library.SpringShelf.Model.TransactionStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private int bookCopyId;
    private String bookTitle;
    private LocalDateTime borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Double lateFee;
    private TransactionStatus status;
}
