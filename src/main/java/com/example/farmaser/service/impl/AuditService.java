package com.example.farmaser.service.impl;

import com.example.farmaser.mapper.auditMapper.AuditLogResponseMapper;
import com.example.farmaser.model.dto.auditDto.AuditLogResponseDto;
import com.example.farmaser.model.entity.ActionType;
import com.example.farmaser.model.entity.AuditLogEntity;
import com.example.farmaser.model.entity.UserEntity;
import com.example.farmaser.model.repository.AuditLogRepository;
import com.example.farmaser.model.repository.UserRepository;
import com.example.farmaser.service.IAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService implements IAudit {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogResponseMapper auditLogResponseMapper;

    @Override
    @Transactional
    public void log(String entityType, Long entityId, ActionType action, Long userId, String oldValue, String newValue, String description, String ipAddress) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        AuditLogEntity auditLog = AuditLogEntity.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .user(user)
                .oldValue(oldValue)
                .newValue(newValue)
                .description(description)
                .ipAddress(ipAddress)
                .build();

        auditLogRepository.save(auditLog);
    }

    @Override
    public Page<AuditLogResponseDto> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public Page<AuditLogResponseDto> getLogsByEntityType(String entityType, Pageable pageable) {
        return auditLogRepository.findByEntityType(entityType, pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public Page<AuditLogResponseDto> getLogsByEntity(String entityType, Long entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public Page<AuditLogResponseDto> getLogsByUser(Long userId, Pageable pageable) {
        return auditLogRepository.findByUserId(userId, pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public Page<AuditLogResponseDto> getLogsByAction(ActionType action, Pageable pageable) {
        return auditLogRepository.findByAction(action, pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public Page<AuditLogResponseDto> getLogsByDateRange(Date start, Date end, Pageable pageable) {
        return auditLogRepository.findByTimestampBetween(start, end, pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public Page<AuditLogResponseDto> getLogsByEntityTypeAndDateRange(String entityType, Date start, Date end, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndTimestampBetween(entityType, start, end, pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public Page<AuditLogResponseDto> getLogsByUserAndDateRange(Long userId, Date start, Date end, Pageable pageable) {
        return auditLogRepository.findByUserIdAndTimestampBetween(userId, start, end, pageable)
                .map(auditLogResponseMapper::toDto);
    }

    @Override
    public List<AuditLogResponseDto> getEntityHistory(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId)
                .stream()
                .map(auditLogResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}

