package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.Dto.TransactionDto;
import com.Library.SpringShelf.Service.ReportService;
import com.Library.SpringShelf.Dto.MostBorrowedBookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Get most borrowed books", description = "Returns a ranked list of the most popular books based on borrowing frequency. Requires ADMIN or LIBRARIAN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report generated successfully"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    @GetMapping("/most-borrowed")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<MostBorrowedBookDto>> getMostBorrowedBooks(
            @RequestParam(defaultValue = "10") int limit) {

        List<MostBorrowedBookDto> report = reportService.getMostBorrowedBooks(limit);
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Get currently overdue items", description = "Returns a list of all book copies that are currently checked out and past their due date. Requires ADMIN or LIBRARIAN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report generated successfully"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    @GetMapping("/overdue-items")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<TransactionDto>> getOverdueItems() {
        List<TransactionDto> report = reportService.getOverdueItems();
        return ResponseEntity.ok(report);
    }
}