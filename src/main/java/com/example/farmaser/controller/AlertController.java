package com.example.farmaser.controller;

import com.example.farmaser.exceptions.NotFoundException;
import com.example.farmaser.model.dto.alertDto.AlertResponseDto;
import com.example.farmaser.model.repository.UserRepository;
import com.example.farmaser.service.IAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    @Autowired
    private IAlert alertService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<AlertResponseDto>> getUserAlerts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {
        Long userId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        return ResponseEntity.ok(alertService.getUserAlerts(userId, pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AlertResponseDto>> getUserAlertsAll() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(alertService.getUserAlerts(userId));
    }

    @GetMapping("/unread")
    public ResponseEntity<Page<AlertResponseDto>> getUserUnreadAlerts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        return ResponseEntity.ok(alertService.getUserUnreadAlerts(userId, pageable));
    }

    @GetMapping("/unread/all")
    public ResponseEntity<List<AlertResponseDto>> getUserUnreadAlertsAll() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(alertService.getUserUnreadAlerts(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<AlertResponseDto> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.markAsRead(id));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        Long userId = getCurrentUserId();
        alertService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"))
                    .getId();
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}

