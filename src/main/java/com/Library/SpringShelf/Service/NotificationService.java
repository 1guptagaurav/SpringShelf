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
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *")
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

            String subject = "Overdue Book Reminder: " + bookTitle;
            String text = "Dear " + borrower.getFirstname() + ",\n\nThis is a friendly reminder that the book '"
                    + bookTitle + "' which you borrowed on " + transaction.getBorrowDate().toLocalDate()
                    + " is overdue. Please return it at your earliest convenience.\n\nThank you,\nSpringShelf Library";

            emailService.sendSimpleMessage(borrower.getEmail(), subject, text);
        }
    }


    @Scheduled(cron = "0 0 10 * * MON")
    public void sendInactiveUserSuggestions() {
        logger.info("Running scheduled task: Sending suggestions to inactive users...");
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        List<User> inactiveUsers = userRepository.findInactiveUsers(thirtyDaysAgo);

        if (inactiveUsers.isEmpty()) {
            logger.info("No inactive users found.");
            return;
        }

        for (User user : inactiveUsers) {

            String subject = "We Miss You at SpringShelf Library!";
            String text = "Dear " + user.getFirstname() + ",\n\nWe've noticed you haven't borrowed a book in a while. "
                    + "Come check out our new arrivals!\n\nBest,\nSpringShelf Library";

            emailService.sendSimpleMessage(user.getEmail(), subject, text);
        }
    }
}