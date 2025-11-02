package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.ProductEntity;
import com.example.farmaser.model.entity.StockMovementEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockMovementRepository extends CrudRepository<StockMovementEntity, Long> {

    List<StockMovementEntity> findByProductOrderByDateDesc(ProductEntity product);

    List<StockMovementEntity> findAllByOrderByDateDesc();
}


