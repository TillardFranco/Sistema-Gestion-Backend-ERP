package com.example.farmaser.mapper.auditMapper;

import com.example.farmaser.model.dto.auditDto.AuditLogResponseDto;
import com.example.farmaser.model.entity.AuditLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditLogResponseMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(expression = "java(entity.getUser() != null ? entity.getUser().getName() + \" \" + entity.getUser().getLastname() : null)", target = "userName")
    @Mapping(source = "user.email", target = "userEmail")
    AuditLogResponseDto toDto(AuditLogEntity entity);
}

