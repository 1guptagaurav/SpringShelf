package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id NOT IN " +
            "(SELECT DISTINCT t.borrower.id FROM BorrowingTransaction t WHERE t.borrowDate >= :sinceDate)")
    List<User> findInactiveUsers(@Param("sinceDate") LocalDate sinceDate);
}
