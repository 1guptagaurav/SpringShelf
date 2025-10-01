package com.Library.SpringShelf.DTO;


import lombok.Data;

@Data
public class BookDto {
    private String title;
    private String author;
    private String description;
    private String isbn;
    private String barcode;
    private String location;
}
