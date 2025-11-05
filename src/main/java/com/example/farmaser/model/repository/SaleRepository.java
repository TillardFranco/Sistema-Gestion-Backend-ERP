package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.SaleEntity;
import com.example.farmaser.model.entity.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.Optional;

public interface SaleRepository extends PagingAndSortingRepository<SaleEntity, Long>, CrudRepository<SaleEntity, Long> {

    Optional<SaleEntity> findBySaleNumber(String saleNumber);

    Page<SaleEntity> findByStatus(SaleStatus status, Pageable pageable);

    Page<SaleEntity> findByDateBetween(Date start, Date end, Pageable pageable);

    @Query("select distinct s from SaleEntity s left join fetch s.items where s.date between :start and :end")
    java.util.List<SaleEntity> findAllWithItemsByDateBetween(@Param("start") Date start, @Param("end") Date end);
}

