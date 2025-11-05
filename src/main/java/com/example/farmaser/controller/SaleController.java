package com.example.farmaser.controller;

import com.example.farmaser.config.PaginationConstants;
import com.example.farmaser.model.dto.saleDto.SaleRequestDto;
import com.example.farmaser.model.dto.saleDto.SaleResponseDto;
import com.example.farmaser.model.entity.SaleStatus;
import com.example.farmaser.service.ISale;
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

import java.util.Date;

@RestController
@RequestMapping("/api/v1/sales")
public class SaleController {

    @Autowired
    private ISale saleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<SaleResponseDto> create(@Valid @RequestBody SaleRequestDto requestDto) {
        String userEmail = getCurrentUserEmail();
        SaleResponseDto created = saleService.create(requestDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'VIEWER')")
    public ResponseEntity<Page<SaleResponseDto>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {
        int validatedSize = PaginationConstants.validateAndLimitPageSize(size, PaginationConstants.MAX_PAGE_SIZE_SALES);
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(sortDir, sortBy));
        return ResponseEntity.ok(saleService.listAll(pageable));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'VIEWER')")
    public ResponseEntity<Page<SaleResponseDto>> listByStatus(
            @PathVariable SaleStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        int validatedSize = PaginationConstants.validateAndLimitPageSize(size, PaginationConstants.MAX_PAGE_SIZE_SALES);
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(Sort.Direction.DESC, "date"));
        return ResponseEntity.ok(saleService.listByStatus(status, pageable));
    }

    @GetMapping("/search/by-sale-number/{saleNumber}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'WAREHOUSE', 'VIEWER')")
    public ResponseEntity<SaleResponseDto> findBySaleNumber(@PathVariable String saleNumber) {
        return ResponseEntity.ok(saleService.findBySaleNumber(saleNumber));
    }

    @GetMapping("/reports/by-date-range")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'VIEWER')")
    public ResponseEntity<Page<SaleResponseDto>> listByDateRange(
            @RequestParam Date start,
            @RequestParam Date end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        int validatedSize = PaginationConstants.validateAndLimitPageSize(size, PaginationConstants.MAX_PAGE_SIZE_SALES);
        Pageable pageable = PageRequest.of(page, validatedSize, Sort.by(Sort.Direction.DESC, "date"));
        return ResponseEntity.ok(saleService.listByDateRange(start, end, pageable));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<SaleResponseDto> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.cancel(id));
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("Usuario no autenticado");
    }
}

