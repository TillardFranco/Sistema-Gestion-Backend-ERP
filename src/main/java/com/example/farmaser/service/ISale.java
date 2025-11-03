package com.example.farmaser.service;

import com.example.farmaser.model.dto.saleDto.SaleRequestDto;
import com.example.farmaser.model.dto.saleDto.SaleResponseDto;
import com.example.farmaser.model.entity.SaleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ISale {
    SaleResponseDto create(SaleRequestDto requestDto, String userEmail);
    Page<SaleResponseDto> listAll(Pageable pageable);
    Page<SaleResponseDto> listByStatus(SaleStatus status, Pageable pageable);
    Page<SaleResponseDto> listByDateRange(Date start, Date end, Pageable pageable);
    SaleResponseDto findBySaleNumber(String saleNumber);
    SaleResponseDto cancel(Long id);
}

