package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.Dto.TransactionDto;
import com.Library.SpringShelf.Model.BorrowingTransaction;
import com.Library.SpringShelf.Model.TransactionStatus;
import com.Library.SpringShelf.Repository.BorrowingTransactionRepository;
import com.Library.SpringShelf.Dto.MostBorrowedBookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final BorrowingTransactionRepository transactionRepository;

    public List<MostBorrowedBookDto> getMostBorrowedBooks(int limit) {
        return transactionRepository.findMostBorrowedBooks(PageRequest.of(0, limit));
    }
    public List<TransactionDto> getOverdueItems() {
        List<BorrowingTransaction> overdueTransactions = transactionRepository
                .findByStatusAndDueDateBefore(TransactionStatus.ACTIVE, LocalDate.now());

        return overdueTransactions.stream()
                .map(this::convertToTransactionDto)
                .collect(Collectors.toList());
    }

    private TransactionDto convertToTransactionDto(BorrowingTransaction transaction) {
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