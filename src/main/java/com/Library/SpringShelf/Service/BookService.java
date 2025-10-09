package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.Aop.Auditable;
import com.Library.SpringShelf.Dto.BookDto;
import com.Library.SpringShelf.Dto.BookResponseDto;
import com.Library.SpringShelf.Exception.ResourceNotFoundException;
import com.Library.SpringShelf.Model.AvailabilityStatus;
import com.Library.SpringShelf.Model.Book;
import com.Library.SpringShelf.Model.BookCopy;
import com.Library.SpringShelf.Repository.BookCopyRepository;
import com.Library.SpringShelf.Repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public Page<BookResponseDto> searchBooks(String title, String author, Pageable pageable) {
        // Start with a specification that does nothing (returns all)
        Specification<Book> spec = Specification.where(null);

        // If a title is provided, add the title criteria to our specification
        if (title != null && !title.isEmpty()) {
            spec = spec.and(BookSpecification.titleContains(title));
        }

        // If an author is provided, add the author criteria
        if (author != null && !author.isEmpty()) {
            spec = spec.and(BookSpecification.authorContains(author));
        }

        // Execute the query with the combined specification and pageable info
        Page<Book> bookPage = bookRepository.findAll(spec, pageable);

        return bookPage.map(this::convertToBookResponseDto);
    }


    @Transactional
    @Auditable(action = "BOOK_ADDED")
    public void addBook(BookDto bookDto){
        Optional<BookCopy> bookCopyAvailable=bookCopyRepository.findByBarcode(bookDto.getBarcode());
        if(bookCopyAvailable.isPresent()){
            throw new RuntimeException("Book Already Exist");
        }
        Optional<Book> availableBook= bookRepository.findBookByIsbn(bookDto.getIsbn());
        if(availableBook.isPresent()){
            BookCopy bookCopy=new BookCopy();
            bookCopy.setBook(availableBook.get());
            bookCopy.setBarcode(bookDto.getBarcode());
            bookCopy.setLocation(bookDto.getLocation());
            bookCopy.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            bookCopyRepository.save(bookCopy);
        }else{
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(bookDto.getAuthor());
            book.setDescription(bookDto.getDescription());
            book.setIsbn(bookDto.getIsbn());

            BookCopy bookCopy=new BookCopy();
            bookCopy.setBook(book);
            bookCopy.setBarcode(bookDto.getBarcode());
            bookCopy.setLocation(bookDto.getLocation());
            bookCopy.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
            bookRepository.save(book);
            bookCopyRepository.save(bookCopy);
        }
    }

    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookPage.map(this::convertToBookResponseDto);
    }

    private BookResponseDto convertToBookResponseDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setDescription(book.getDescription());
        dto.setIsbn(book.getIsbn());

        int availableCount = bookCopyRepository.countByBookAndAvailabilityStatus(
                book,
                AvailabilityStatus.AVAILABLE
        );
        dto.setAvailableCopies(availableCount);
        return dto;
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToBookResponseDto(book);
    }

    public List<BookResponseDto> getAvailableBooks() {
        List<Book> availableBooks = bookRepository.findBooksWithAvailableCopies(AvailabilityStatus.AVAILABLE);
        return availableBooks.stream()
                .map(this::convertToBookResponseDto)
                .toList();
    }

    @Transactional
    @Auditable(action = "BOOK_DETAILS_UPDATED")
    public BookResponseDto updateBook(Long id, BookDto bookRequest) {
        // 1. Find the existing book or throw an exception
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // 2. Update the properties of the existing entity
        existingBook.setTitle(bookRequest.getTitle()!=null ?bookRequest.getTitle() :existingBook.getTitle());
        existingBook.setAuthor(bookRequest.getAuthor()!=null?bookRequest.getAuthor():existingBook.getAuthor());
        existingBook.setDescription(bookRequest.getDescription()!=null?bookRequest.getDescription():existingBook.getDescription());
        existingBook.setIsbn(bookRequest.getIsbn()!=null?bookRequest.getIsbn():existingBook.getIsbn());

        // 3. Save the updated entity (JPA will perform an UPDATE query)
        Book savedBook = bookRepository.save(existingBook);

        // 4. Convert the updated entity back to a response DTO
        return convertToBookResponseDto(savedBook);
    }

    // In BookCopyService.java
//    @Transactional
//    public BookCopyDto updateBookCopy(int copyId, BookCopyUpdateDto updateDto) {
//        BookCopy existingCopy = bookCopyRepository.findById(copyId)
//                .orElseThrow(() -> new RuntimeException("BookCopy not found with id: " + copyId));
//
//        if (updateDto.getLocation() != null) {
//            existingCopy.setLocation(updateDto.getLocation());
//        }
//        if (updateDto.getAvailabilityStatus() != null) {
//            existingCopy.setAvailabilityStatus(updateDto.getAvailabilityStatus());
//        }
//        // You could also add barcode to the BookCopyUpdateDto if needed
//
//        BookCopy savedCopy = bookCopyRepository.save(existingCopy);
//        return convertToBookCopyDto(savedCopy);
//    }
}
