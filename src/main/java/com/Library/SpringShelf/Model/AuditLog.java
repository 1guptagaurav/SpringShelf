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
    private String username; // Who performed the action

    @Column(nullable = false)
    private String action; // What action was performed

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp; // When it happened

    @Lob
    @Column(columnDefinition = "TEXT")
    private String details; // JSON details of the action

    @PrePersist
    public void onPrePersist() {
        // Automatically set the timestamp before saving
        this.timestamp = LocalDateTime.now();
    }
}