package com.Library.SpringShelf.Repository;

import com.Library.SpringShelf.Model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}