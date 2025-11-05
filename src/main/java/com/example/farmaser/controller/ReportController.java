package com.example.farmaser.controller;

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

    @GetMapping("/sales/summary")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public SalesSummaryDto getSalesSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        return reportService.getSalesSummary(start, end);
    }

    @GetMapping("/sales/top-products")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public List<TopProductDto> getTopProducts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end,
            @RequestParam(defaultValue = "10") int limit) {
        int effectiveLimit = Math.max(1, Math.min(limit, 100));
        return reportService.getTopProducts(start, end, effectiveLimit);
    }

    @GetMapping("/sales/by-seller")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public List<SellerPerformanceDto> getSellerPerformance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        return reportService.getSellerPerformance(start, end);
    }

    @GetMapping("/sales/daily")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','MANAGER')")
    public List<DailySalesPointDto> getDailySeries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        return reportService.getDailySalesSeries(start, end);
    }
}


