package com.Library.SpringShelf.DTO;
import lombok.Data;

@Data
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private String description;
    private String isbn;
    private int availableCopies;
}
