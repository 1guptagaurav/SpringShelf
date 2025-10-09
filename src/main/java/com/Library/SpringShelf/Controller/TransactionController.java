package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.Dto.BorrowRequestDto;
import com.Library.SpringShelf.Dto.ReturnRequestDto;
import com.Library.SpringShelf.Dto.TransactionDto;
import com.Library.SpringShelf.Service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Borrow a book copy", description = "Creates a new borrowing transaction for a member. Requires LIBRARIAN or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "User does not have permission"),
            @ApiResponse(responseCode = "404", description = "User or Book Copy not found")
    })
    @PostMapping("/borrow")
    public ResponseEntity<TransactionDto> borrowBook(@Valid @RequestBody BorrowRequestDto borrowRequest, Authentication authentication ) {
        String username = authentication.getName();
        TransactionDto transaction = transactionService.borrowBook(borrowRequest,username);
        return ResponseEntity.ok(transaction);
    }

    @Operation(summary = "Return a book copy", description = "Marks a borrowed book as returned. Requires LIBRARIAN or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "404", description = "Active transaction for this book copy not found")
    })
    @PostMapping("/return")
    public ResponseEntity<TransactionDto> returnBook(@Valid @RequestBody ReturnRequestDto returnRequest,Authentication authentication) {
        String username = authentication.getName();
        TransactionDto transaction = transactionService.returnBook(returnRequest,username);
        return ResponseEntity.ok(transaction);
    }
}