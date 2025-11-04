package com.example.farmaser.service;

import com.example.farmaser.model.dto.alertDto.AlertResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAlert {
    void generateExpirationAlerts();
    Page<AlertResponseDto> getUserAlerts(Long userId, Pageable pageable);
    List<AlertResponseDto> getUserAlerts(Long userId);
    List<AlertResponseDto> getUserUnreadAlerts(Long userId);
    Page<AlertResponseDto> getUserUnreadAlerts(Long userId, Pageable pageable);
    AlertResponseDto markAsRead(Long alertId);
    void markAllAsRead(Long userId);
}

