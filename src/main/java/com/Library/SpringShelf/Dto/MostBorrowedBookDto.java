package com.Library.SpringShelf.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostBorrowedBookDto {
    private Long bookId;
    private String title;
    private String author;
    private Long borrowCount;
}
