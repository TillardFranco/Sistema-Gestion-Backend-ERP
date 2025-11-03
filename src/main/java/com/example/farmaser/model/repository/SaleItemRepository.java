package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.SaleEntity;
import com.example.farmaser.model.entity.SaleItemEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaleItemRepository extends CrudRepository<SaleItemEntity, Long> {
    List<SaleItemEntity> findBySale(SaleEntity sale);
}

