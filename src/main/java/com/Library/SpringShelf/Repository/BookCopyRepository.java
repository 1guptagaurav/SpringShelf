package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Model.AvailabilityStatus;
import com.Library.SpringShelf.Model.Book;
import com.Library.SpringShelf.Model.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy,Long> {
    Optional<BookCopy> findByBarcode(String Barcode);
    int countByBookAndAvailabilityStatus(Book book, AvailabilityStatus status);
}
