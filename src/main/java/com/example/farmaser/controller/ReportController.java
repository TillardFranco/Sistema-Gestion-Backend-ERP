package com.example.farmaser.controller;

import com.example.farmaser.exceptions.BadRequestException;
import com.example.farmaser.model.dto.reportDto.DailySalesPointDto;
import com.example.farmaser.model.dto.reportDto.SalesSummaryDto;
import com.example.farmaser.model.dto.reportDto.SellerPerformanceDto;
import com.example.farmaser.model.dto.reportDto.TopProductDto;
import com.example.farmaser.service.IReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private IReport reportService;

    private static final long MAX_DATE_RANGE_MS = 365L * 24 * 60 * 60 * 1000; // 1 año en milisegundos

    private void validateDateRange(Date start, Date end) {
        if (start == null || end == null) {
            throw new BadRequestException("Las fechas de inicio y fin son requeridas");
        }
        if (start.after(end)) {
            throw new BadRequestException("La fecha de inicio debe ser anterior a la fecha de fin");
        }
        long rangeMs = end.getTime() - start.getTime();
        if (rangeMs > MAX_DATE_RANGE_MS) {
            throw new BadRequestException("El rango de fechas no puede exceder 1 año");
        }
    }

    @GetMapping("/sales/summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public SalesSummaryDto getSalesSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        validateDateRange(start, end);
        return reportService.getSalesSummary(start, end);
    }

    @GetMapping("/sales/top-products")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public List<TopProductDto> getTopProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end,
            @RequestParam(defaultValue = "10") int limit) {
        validateDateRange(start, end);
        int effectiveLimit = Math.max(1, Math.min(limit, 100));
        return reportService.getTopProducts(start, end, effectiveLimit);
    }

    @GetMapping("/sales/by-seller")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public List<SellerPerformanceDto> getSellerPerformance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        validateDateRange(start, end);
        return reportService.getSellerPerformance(start, end);
    }

    @GetMapping("/sales/daily")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public List<DailySalesPointDto> getDailySeries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        validateDateRange(start, end);
        return reportService.getDailySalesSeries(start, end);
    }
}


