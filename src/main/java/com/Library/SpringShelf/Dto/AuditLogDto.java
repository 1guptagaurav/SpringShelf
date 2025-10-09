package com.Library.SpringShelf.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditLogDto {

    private Long id;
    private String username;
    private String action;
    private LocalDateTime timestamp;
    private Object details;
}