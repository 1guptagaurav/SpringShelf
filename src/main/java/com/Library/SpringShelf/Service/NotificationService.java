package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.Model.BorrowingTransaction;
import com.Library.SpringShelf.Model.TransactionStatus;
import com.Library.SpringShelf.Model.User;
import com.Library.SpringShelf.Repository.BorrowingTransactionRepository;
import com.Library.SpringShelf.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final BorrowingTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * This task runs every day at 9:00 AM.
     * It finds all active transactions that are past their due date and sends a reminder.
     */
    @Scheduled(cron = "0 0 9 * * *") // Runs at 9:00:00 AM every day
    public void sendOverdueReminders() {
        logger.info("Running scheduled task: Sending overdue reminders...");
        List<BorrowingTransaction> overdueTransactions = transactionRepository
                .findByStatusAndDueDateBefore(TransactionStatus.ACTIVE, LocalDate.now());

        if (overdueTransactions.isEmpty()) {
            logger.info("No overdue books found.");
            return;
        }

        for (BorrowingTransaction transaction : overdueTransactions) {
            User borrower = transaction.getBorrower();
            String bookTitle = transaction.getBookCopy().getBook().getTitle();

            // In a real application, you would use an email service here.
            // For now, we'll just log it to the console.
            logger.info("Sending reminder to {}: Book '{}' is overdue.", borrower.getEmail(), bookTitle);
        }
    }

    /**
     * This task runs every Monday at 10:00 AM.
     * It finds users who haven't borrowed a book in the last 30 days and sends a suggestion.
     */
    @Scheduled(cron = "0 0 10 * * MON") // Runs at 10:00:00 AM every Monday
    public void sendInactiveUserSuggestions() {
        logger.info("Running scheduled task: Sending suggestions to inactive users...");
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<User> inactiveUsers = userRepository.findInactiveUsers(thirtyDaysAgo);

        if (inactiveUsers.isEmpty()) {
            logger.info("No inactive users found.");
            return;
        }

        for (User user : inactiveUsers) {
            // In a real application, you would find book suggestions and email them.
            // For now, we'll just log it.
            logger.info("Sending activity suggestion to inactive user: {}", user.getEmail());
        }
    }
}