package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Model.AvailabilityStatus;
import com.Library.SpringShelf.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findBookByIsbn(String isbn);

//    @Query("SELECT DISTINCT b FROM Book b JOIN b.copies c WHERE c.availabilityStatus = AvailabilityStatus.AVAILABLE")
//    List<Book> findBooksWithAvailableCopies();
    @Query("SELECT DISTINCT b FROM Book b JOIN b.copies c WHERE c.availabilityStatus = :status")
    List<Book> findBooksWithAvailableCopies(@Param("status") AvailabilityStatus status);

}
