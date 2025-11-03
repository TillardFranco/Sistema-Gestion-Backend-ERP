package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.SaleEntity;
import com.example.farmaser.model.entity.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.Optional;

public interface SaleRepository extends PagingAndSortingRepository<SaleEntity, Long>, CrudRepository<SaleEntity, Long> {

    Optional<SaleEntity> findBySaleNumber(String saleNumber);

    Page<SaleEntity> findByStatus(SaleStatus status, Pageable pageable);

    Page<SaleEntity> findByDateBetween(Date start, Date end, Pageable pageable);
}

