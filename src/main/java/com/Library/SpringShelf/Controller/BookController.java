package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.Dto.BookDto;
import com.Library.SpringShelf.Dto.BookResponseDto;
import com.Library.SpringShelf.Service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Add a new book/copy", description = "Creates a new book record if ISBN is new, and always adds a new copy. Requires ADMIN or LIBRARIAN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book/copy added successfully"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Void> addBook(@Valid @RequestBody BookDto bookDto) {
        bookService.addBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Get all searched books (paginated)", description = "Returns a paginated and sortable list of all books in the catalog. Publicly accessible.")
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseDto>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            Pageable pageable) {

        Page<BookResponseDto> booksPage = bookService.searchBooks(title, author, pageable);
        return ResponseEntity.ok(booksPage);
    }


    @Operation(summary = "Get all books (paginated)", description = "Returns a paginated and sortable list of all books in the catalog. Publicly accessible.")
    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(Pageable pageable) {
        Page<BookResponseDto> booksPage = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(booksPage);
    }


    @Operation(summary = "Get Specific book with given id (paginated)", description = "Return the paginated and sortable book with id in the catalog. Publicly accessible.")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long id) {
        BookResponseDto book = bookService.getBookById(id); // Method name can be refactored in service
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Get all available books", description = "Returns a paginated and sortable list of all books in the catalog. Publicly accessible.")
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