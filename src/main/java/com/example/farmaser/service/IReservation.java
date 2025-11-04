package com.example.farmaser.service;

import com.example.farmaser.model.dto.reservationDto.ReservationRequestDto;
import com.example.farmaser.model.dto.reservationDto.ReservationResponseDto;
import com.example.farmaser.model.dto.saleDto.SaleRequestDto;
import com.example.farmaser.model.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReservation {
    ReservationResponseDto create(ReservationRequestDto requestDto);
    Page<ReservationResponseDto> listAll(Pageable pageable);
    Page<ReservationResponseDto> listByStatus(ReservationStatus status, Pageable pageable);
    ReservationResponseDto findById(Long id);
    ReservationResponseDto findByReservationNumber(String reservationNumber);
    ReservationResponseDto confirm(Long id);
    ReservationResponseDto complete(Long id, SaleRequestDto saleRequestDto, String userEmail);
    ReservationResponseDto cancel(Long id);
    void expireReservations();
}

