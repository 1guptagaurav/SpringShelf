package com.Library.SpringShelf.Controller;


import com.Library.SpringShelf.DTO.BookDto;
import com.Library.SpringShelf.DTO.BookResponseDto;
import com.Library.SpringShelf.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add-books")
    private Boolean addBooks(@Valid @RequestBody BookDto bookDto){
        bookService.addBooks(bookDto);
        return true;
    }
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        List<BookResponseDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/get-book-by-id/{id}")
    private ResponseEntity<BookResponseDto> searchBookById(@PathVariable Long id){
        BookResponseDto book= bookService.searchBookById(id);
        return ResponseEntity.ok(book);
    }
    @GetMapping("/available")
    public ResponseEntity<List<BookResponseDto>> getAvailableBooks() {
        List<BookResponseDto> books = bookService.getAvailableBooks();
        return ResponseEntity.ok(books);
    }
//    Use BookRequestDto for the update data, but return BookResponseDto

//    @PutMapping("/{id}")
//    public ResponseEntity<BookResponseDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto bookRequest) {
//        BookResponseDto updatedBook = bookService.updateBook(id, bookRequest);
//        return ResponseEntity.ok(updatedBook);
//    }
}
