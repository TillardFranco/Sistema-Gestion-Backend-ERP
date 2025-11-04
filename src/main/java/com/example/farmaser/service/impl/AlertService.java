package com.example.farmaser.service.impl;

import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.mapper.alertMapper.AlertResponseMapper;
import com.example.farmaser.model.dto.alertDto.AlertResponseDto;
import com.example.farmaser.model.entity.*;
import com.example.farmaser.model.repository.AlertRepository;
import com.example.farmaser.model.repository.ProductRepository;
import com.example.farmaser.model.repository.UserRepository;
import com.example.farmaser.service.IAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService implements IAlert {

    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlertResponseMapper alertResponseMapper;

    @Transactional
    @Override
    public void generateExpirationAlerts() {
        logger.info("Iniciando generación de alertas de productos próximos a vencer...");

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        
        // Normalizar hoy a medianoche para comparaciones precisas
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayMidnight = calendar.getTime();

        // Calcular fechas límite para alertas
        // 1 mes antes: entre 28 y 31 días antes de vencer
        calendar.setTime(todayMidnight);
        calendar.add(Calendar.DAY_OF_MONTH, 28);
        Date oneMonthBefore = calendar.getTime();
        
        calendar.setTime(todayMidnight);
        calendar.add(Calendar.DAY_OF_MONTH, 31);
        Date oneMonthBeforeEnd = calendar.getTime();

        // 1 semana antes: entre 5 y 7 días antes de vencer
        calendar.setTime(todayMidnight);
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        Date oneWeekBefore = calendar.getTime();
        
        calendar.setTime(todayMidnight);
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date oneWeekBeforeEnd = calendar.getTime();

        // Hoy: mismo día
        calendar.setTime(todayMidnight);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrowMidnight = calendar.getTime();

        // Obtener todos los productos activos con fecha de vencimiento
        List<ProductEntity> products = productRepository.findByActiveTrue().stream()
                .filter(product -> product.getExpirationDate() != null)
                .collect(Collectors.toList());

        // Obtener todos los usuarios (para crear alertas para todos)
        List<UserEntity> users = ((List<UserEntity>) userRepository.findAll());

        int alertsCreated = 0;

        for (ProductEntity product : products) {
            Date expirationDate = product.getExpirationDate();
            
            // Normalizar fecha de vencimiento a medianoche
            calendar.setTime(expirationDate);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date expirationDateMidnight = calendar.getTime();

            // Calcular días hasta vencimiento
            long daysUntilExpiration = (expirationDateMidnight.getTime() - todayMidnight.getTime()) / (1000 * 60 * 60 * 24);

            // Verificar si el producto ya tiene una alerta no leída del mismo tipo (evitar duplicados)

            // Alertas de "hoy" (vencen hoy, días = 0)
            if (daysUntilExpiration == 0) {
                if (!alertRepository.existsByProductIdAndTypeAndReadFalse(product.getId(), AlertType.EXPIRING_PRODUCT_TODAY)) {
                    createAlertForAllUsers(users, product, AlertType.EXPIRING_PRODUCT_TODAY, 
                        String.format("🚨 PRODUCTO VENCE HOY: %s (Vence: %s)", product.getName(), formatDate(expirationDate)), expirationDate);
                    alertsCreated++;
                }
            }
            // Alertas de "1 semana antes" (vencen en 5-7 días)
            else if (daysUntilExpiration >= 5 && daysUntilExpiration <= 7) {
                if (!alertRepository.existsByProductIdAndTypeAndReadFalse(product.getId(), AlertType.EXPIRING_PRODUCT_1_WEEK)) {
                    createAlertForAllUsers(users, product, AlertType.EXPIRING_PRODUCT_1_WEEK, 
                        String.format("⚠️ PRODUCTO VENCE EN 1 SEMANA: %s (Vence: %s - Faltan %d días)", 
                            product.getName(), formatDate(expirationDate), daysUntilExpiration), expirationDate);
                    alertsCreated++;
                }
            }
            // Alertas de "1 mes antes" (vencen en 28-31 días)
            else if (daysUntilExpiration >= 28 && daysUntilExpiration <= 31) {
                if (!alertRepository.existsByProductIdAndTypeAndReadFalse(product.getId(), AlertType.EXPIRING_PRODUCT_1_MONTH)) {
                    createAlertForAllUsers(users, product, AlertType.EXPIRING_PRODUCT_1_MONTH, 
                        String.format("⚠️ PRODUCTO VENCE EN 1 MES: %s (Vence: %s - Faltan %d días)", 
                            product.getName(), formatDate(expirationDate), daysUntilExpiration), expirationDate);
                    alertsCreated++;
                }
            }
        }

        logger.info("Generación de alertas completada. Alertas creadas: {}", alertsCreated);
    }

    private void createAlertForAllUsers(List<UserEntity> users, ProductEntity product, AlertType type, String message, Date expirationDate) {
        for (UserEntity user : users) {
            AlertEntity alert = AlertEntity.builder()
                    .type(type)
                    .message(message)
                    .read(false)
                    .user(user)
                    .product(product)
                    .expirationDate(expirationDate)
                    .build();
            alertRepository.save(alert);
        }
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.format("%02d/%02d/%04d", cal.get(Calendar.DAY_OF_MONTH), 
            cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AlertResponseDto> getUserAlerts(Long userId, Pageable pageable) {
        Page<AlertEntity> page = alertRepository.findByUserId(userId, pageable);
        List<AlertResponseDto> dtos = page.getContent().stream()
                .map(alertResponseMapper::alertEntityToAlertResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlertResponseDto> getUserAlerts(Long userId) {
        List<AlertEntity> alerts = alertRepository.findByUserId(userId);
        return alerts.stream()
                .map(alertResponseMapper::alertEntityToAlertResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AlertResponseDto> getUserUnreadAlerts(Long userId) {
        List<AlertEntity> alerts = alertRepository.findByUserIdAndReadFalse(userId);
        return alerts.stream()
                .map(alertResponseMapper::alertEntityToAlertResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AlertResponseDto> getUserUnreadAlerts(Long userId, Pageable pageable) {
        Page<AlertEntity> page = alertRepository.findByUserIdAndReadFalse(userId, pageable);
        List<AlertResponseDto> dtos = page.getContent().stream()
                .map(alertResponseMapper::alertEntityToAlertResponseDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Transactional
    @Override
    public AlertResponseDto markAsRead(Long alertId) {
        AlertEntity alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new NotFoundException("Alerta con id " + alertId + " no encontrada"));
        alert.setRead(true);
        AlertEntity saved = alertRepository.save(alert);
        return alertResponseMapper.alertEntityToAlertResponseDto(saved);
    }

    @Transactional
    @Override
    public void markAllAsRead(Long userId) {
        List<AlertEntity> unreadAlerts = alertRepository.findByUserIdAndReadFalse(userId);
        for (AlertEntity alert : unreadAlerts) {
            alert.setRead(true);
            alertRepository.save(alert);
        }
    }
}

