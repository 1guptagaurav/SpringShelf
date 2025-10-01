package com.Library.SpringShelf.DTO;

import lombok.Data;
@Data
public class BorrowRequestDto {
    private Long userId;
    private Long bookCopyId;
}