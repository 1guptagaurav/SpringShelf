package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.DTO.BookDto;
import com.Library.SpringShelf.DTO.BookResponseDto;
import com.Library.SpringShelf.Service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // Use standard POST on the base path, and return a proper response
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Void> addBook(@Valid @RequestBody BookDto bookDto) {
        bookService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // This was already good
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    // Use a simpler, more standard path
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        BookResponseDto book = bookService.getBookById(id); // Method name can be refactored in service
        return ResponseEntity.ok(book);
    }

    // This was already good
    @GetMapping("/available")
    public ResponseEntity<List<BookResponseDto>> getAvailableBooks() {
        List<BookResponseDto> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(books);
    }

    // Example of a secured update endpoint (currently commented out in your code)
    /*
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto bookRequest) {
        BookResponseDto updatedBook = bookService.updateBook(id, bookRequest);
        return ResponseEntity.ok(updatedBook);
    }
    */
}