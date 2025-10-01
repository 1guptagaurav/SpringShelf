package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.DTO.BorrowRequestDto;
import com.Library.SpringShelf.DTO.ReturnRequestDto;
import com.Library.SpringShelf.DTO.TransactionDto;
import com.Library.SpringShelf.Service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/borrow")
    public ResponseEntity<TransactionDto> borrowBook(@Valid @RequestBody BorrowRequestDto borrowRequest) {
        TransactionDto transaction = transactionService.borrowBook(borrowRequest);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/return")
    public ResponseEntity<TransactionDto> returnBook(@Valid @RequestBody ReturnRequestDto returnRequest) {
        TransactionDto transaction = transactionService.returnBook(returnRequest);
        return ResponseEntity.ok(transaction);
    }
}