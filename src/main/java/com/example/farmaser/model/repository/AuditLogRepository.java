package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.AuditLogEntity;
import com.example.farmaser.model.entity.ActionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface AuditLogRepository extends PagingAndSortingRepository<AuditLogEntity, Long>, CrudRepository<AuditLogEntity, Long> {

    Page<AuditLogEntity> findByEntityType(String entityType, Pageable pageable);

    Page<AuditLogEntity> findByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

    Page<AuditLogEntity> findByUserId(Long userId, Pageable pageable);

    Page<AuditLogEntity> findByAction(ActionType action, Pageable pageable);

    Page<AuditLogEntity> findByTimestampBetween(Date start, Date end, Pageable pageable);

    Page<AuditLogEntity> findByEntityTypeAndTimestampBetween(String entityType, Date start, Date end, Pageable pageable);

    Page<AuditLogEntity> findByUserIdAndTimestampBetween(Long userId, Date start, Date end, Pageable pageable);

    List<AuditLogEntity> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, Long entityId);
}

