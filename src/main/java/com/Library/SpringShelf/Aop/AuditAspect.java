package com.Library.SpringShelf.Aop;
import com.Library.SpringShelf.Model.AuditLog;
import com.Library.SpringShelf.Repository.AuditLogRepository;
import com.Library.SpringShelf.Utils.StringCryptoConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;
    private final StringCryptoConverter cryptoConverter;

    @Pointcut("@annotation(auditable)")
    public void auditablePointcut(Auditable auditable) {}

    @AfterReturning(pointcut = "auditablePointcut(auditable)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Auditable auditable, Object result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "SYSTEM";

        AuditLog auditLog = new AuditLog();
        auditLog.setUsername(username);
        auditLog.setAction(auditable.action());

        Map<String, Object> details = new HashMap<>();
        details.put("arguments", joinPoint.getArgs());
        details.put("returnValue", result);

        try {
            objectMapper.registerModule(new JavaTimeModule());
            String detailsJson = objectMapper.writeValueAsString(details);
            String encryptedDetails = cryptoConverter.convertToDatabaseColumn(detailsJson);
            auditLog.setDetails(encryptedDetails);

        } catch (JsonProcessingException e) {
            auditLog.setDetails("Error: Could not serialize details to JSON.");
        }

        auditLogRepository.save(auditLog);
    }
}