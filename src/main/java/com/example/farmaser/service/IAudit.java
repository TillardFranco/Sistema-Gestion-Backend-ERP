package com.example.farmaser.service;

import com.example.farmaser.model.dto.auditDto.AuditLogResponseDto;
import com.example.farmaser.model.entity.ActionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface IAudit {

    void log(String entityType, Long entityId, ActionType action, Long userId, String oldValue, String newValue, String description, String ipAddress);

    Page<AuditLogResponseDto> getAllLogs(Pageable pageable);

    Page<AuditLogResponseDto> getLogsByEntityType(String entityType, Pageable pageable);

    Page<AuditLogResponseDto> getLogsByEntity(String entityType, Long entityId, Pageable pageable);

    Page<AuditLogResponseDto> getLogsByUser(Long userId, Pageable pageable);

    Page<AuditLogResponseDto> getLogsByAction(ActionType action, Pageable pageable);

    Page<AuditLogResponseDto> getLogsByDateRange(Date start, Date end, Pageable pageable);

    Page<AuditLogResponseDto> getLogsByEntityTypeAndDateRange(String entityType, Date start, Date end, Pageable pageable);

    Page<AuditLogResponseDto> getLogsByUserAndDateRange(Long userId, Date start, Date end, Pageable pageable);

    List<AuditLogResponseDto> getEntityHistory(String entityType, Long entityId);
}

