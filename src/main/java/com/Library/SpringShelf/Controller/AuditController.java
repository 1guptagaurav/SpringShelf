package com.Library.SpringShelf.Controller;

import com.Library.SpringShelf.Dto.AuditLogDto;
import com.Library.SpringShelf.Service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
//import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get system audit logs (Admin only)", description = "Returns a paginated list of all audit trail records.")
    public ResponseEntity<Page<AuditLogDto>> getAuditLogs(Pageable pageable) {
        Page<AuditLogDto> logs = auditService.getAuditLogs(pageable);
        return ResponseEntity.ok(logs);
    }
}