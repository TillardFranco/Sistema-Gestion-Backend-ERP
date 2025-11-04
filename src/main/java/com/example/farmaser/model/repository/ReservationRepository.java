package com.example.farmaser.model.repository;

import com.example.farmaser.model.entity.ReservationEntity;
import com.example.farmaser.model.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends PagingAndSortingRepository<ReservationEntity, Long>, CrudRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findByReservationNumber(String reservationNumber);

    Page<ReservationEntity> findByStatus(ReservationStatus status, Pageable pageable);

    List<ReservationEntity> findByStatus(ReservationStatus status);

    Page<ReservationEntity> findByCustomerId(Long customerId, Pageable pageable);

    Page<ReservationEntity> findByProductId(Long productId, Pageable pageable);

    List<ReservationEntity> findByExpirationDateBeforeAndStatus(Date date, ReservationStatus status);
}

