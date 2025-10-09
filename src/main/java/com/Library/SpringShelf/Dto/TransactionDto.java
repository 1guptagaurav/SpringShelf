package com.Library.SpringShelf.Dto;

import com.Library.SpringShelf.Model.TransactionStatus;
import com.Library.SpringShelf.Utils.EmailMaskingSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private Long id;
    private Long userId;

    @JsonSerialize(using = EmailMaskingSerializer.class)
    private String userEmail;

    private int bookCopyId;
    private String bookTitle;
    private LocalDateTime borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Double lateFee;
    private TransactionStatus status;
}
