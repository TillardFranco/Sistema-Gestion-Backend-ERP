package com.example.farmaser.model.dto.auditDto;

import com.example.farmaser.model.entity.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDto {

    private Long id;
    private String entityType;
    private Long entityId;
    private ActionType action;
    private Long userId;
    private String userName;
    private String userEmail;
    private String oldValue;
    private String newValue;
    private String description;
    private String ipAddress;
    private Date timestamp;
}

