package com.Library.SpringShelf.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String action;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String details;

    @PrePersist
    public void onPrePersist() {
        this.timestamp = LocalDateTime.now();
    }
}