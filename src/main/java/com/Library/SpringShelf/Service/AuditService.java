package com.Library.SpringShelf.Service;

import com.Library.SpringShelf.Dto.AuditLogDto;
import com.Library.SpringShelf.Model.AuditLog;
import com.Library.SpringShelf.Repository.AuditLogRepository;
import com.Library.SpringShelf.Utils.StringCryptoConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final StringCryptoConverter cryptoConverter;
    private final ObjectMapper objectMapper;

    public Page<AuditLogDto> getAuditLogs(Pageable pageable) {
        Page<AuditLog> logPage = auditLogRepository.findAll(pageable);
        return logPage.map(this::convertToDto);
    }

    private AuditLogDto convertToDto(AuditLog auditLog) {
        AuditLogDto dto = new AuditLogDto();
        dto.setId(auditLog.getId());
        dto.setUsername(auditLog.getUsername());
        dto.setAction(auditLog.getAction());
        dto.setTimestamp(auditLog.getTimestamp());

        try {

            String decryptedJson = cryptoConverter. convertToEntityAttribute(auditLog.getDetails());

            Object detailsObject = objectMapper.readValue(decryptedJson, Object.class);
            dto.setDetails(detailsObject);
        } catch (JsonProcessingException e) {
            dto.setDetails("Error: Could not parse details.");
        }

        return dto;
    }
}