package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductoRepository extends CrudRepository <ProductEntity, Long> {
}
