package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.ProductEntity;
import com.example.farmaser.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository <ProductEntity, Long> {

    Optional<ProductEntity> findByBarcode(String barcode);
    boolean existsByBarcode(String barcode);
}
