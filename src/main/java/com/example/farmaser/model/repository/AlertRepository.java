package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.AlertEntity;
import com.example.farmaser.model.entity.AlertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AlertRepository extends PagingAndSortingRepository<AlertEntity, Long>, CrudRepository<AlertEntity, Long> {

    Page<AlertEntity> findByUserIdAndReadFalse(Long userId, Pageable pageable);

    List<AlertEntity> findByUserIdAndReadFalse(Long userId);

    Page<AlertEntity> findByUserId(Long userId, Pageable pageable);

    List<AlertEntity> findByUserId(Long userId);

    List<AlertEntity> findByTypeAndReadFalse(AlertType type);

    boolean existsByProductIdAndTypeAndReadFalse(Long productId, AlertType type);
}

