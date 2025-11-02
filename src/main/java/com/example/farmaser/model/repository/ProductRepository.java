package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long>, CrudRepository<ProductEntity, Long> {

    Optional<ProductEntity> findByBarcode(String barcode);
    boolean existsByBarcode(String barcode);

    List<ProductEntity> findByNameContainingIgnoreCase(String name);
    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<ProductEntity> findByActiveTrue(Pageable pageable);
    List<ProductEntity> findByActiveTrue();

    List<ProductEntity> findByStockLessThanEqual(Integer stock);
    List<ProductEntity> findByStockLessThanEqualAndActiveTrue(Integer stock);
}
