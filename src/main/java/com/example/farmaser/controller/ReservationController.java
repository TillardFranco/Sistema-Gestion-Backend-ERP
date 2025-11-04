package com.example.farmaser.controller;

import com.example.farmaser.model.dto.reservationDto.ReservationRequestDto;
import com.example.farmaser.model.dto.reservationDto.ReservationResponseDto;
import com.example.farmaser.model.dto.saleDto.SaleRequestDto;
import com.example.farmaser.model.entity.ReservationStatus;
import com.example.farmaser.service.IReservation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private IReservation reservationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ReservationResponseDto> create(@Valid @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto created = reservationService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER', 'VIEWER')")
    public ResponseEntity<Page<ReservationResponseDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reservationDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortBy));
        return ResponseEntity.ok(reservationService.listAll(pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER', 'VIEWER')")
    public ResponseEntity<Page<ReservationResponseDto>> listByStatus(
            @PathVariable ReservationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reservationDate"));
        return ResponseEntity.ok(reservationService.listByStatus(status, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER', 'VIEWER')")
    public ResponseEntity<ReservationResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @GetMapping("/search/by-reservation-number/{reservationNumber}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER', 'VIEWER')")
    public ResponseEntity<ReservationResponseDto> findByReservationNumber(@PathVariable String reservationNumber) {
        return ResponseEntity.ok(reservationService.findByReservationNumber(reservationNumber));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<ReservationResponseDto> confirm(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirm(id));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ReservationResponseDto> complete(
            @PathVariable Long id,
            @Valid @RequestBody SaleRequestDto saleRequestDto) {
        String userEmail = getCurrentUserEmail();
        return ResponseEntity.ok(reservationService.complete(id, saleRequestDto, userEmail));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ReservationResponseDto> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancel(id));
    }

    @DeleteMapping("/expired")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<Void> expireReservations() {
        reservationService.expireReservations();
        return ResponseEntity.noContent().build();
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}

