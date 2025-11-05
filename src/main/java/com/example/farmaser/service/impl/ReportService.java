package com.example.farmaser.service.impl;

import com.example.farmaser.model.dto.reportDto.DailySalesPointDto;
import com.example.farmaser.model.dto.reportDto.SalesSummaryDto;
import com.example.farmaser.model.dto.reportDto.SellerPerformanceDto;
import com.example.farmaser.model.dto.reportDto.TopProductDto;
import com.example.farmaser.model.entity.SaleEntity;
import com.example.farmaser.model.entity.SaleItemEntity;
import com.example.farmaser.model.repository.SaleRepository;
import com.example.farmaser.service.IReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService implements IReport {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    @Transactional(readOnly = true)
    public SalesSummaryDto getSalesSummary(Date start, Date end) {
        List<SaleEntity> sales = saleRepository.findAllWithItemsByDateBetween(start, end);

        long totalSales = sales.size();
        long totalItems = sales.stream()
                .flatMap(s -> s.getItems().stream())
                .mapToLong(item -> Optional.ofNullable(item.getQuantity()).orElse(0))
                .sum();

        BigDecimal totalRevenue = sales.stream()
                .map(SaleEntity::getTotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTax = sales.stream()
                .map(SaleEntity::getTax)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSubtotal = sales.stream()
                .map(SaleEntity::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return SalesSummaryDto.builder()
                .startDate(start)
                .endDate(end)
                .totalSalesCount(totalSales)
                .totalItemsSold(totalItems)
                .totalRevenue(totalRevenue)
                .totalTax(totalTax)
                .totalSubtotal(totalSubtotal)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopProductDto> getTopProducts(Date start, Date end, int limit) {
        List<SaleEntity> sales = saleRepository.findAllWithItemsByDateBetween(start, end);

        Map<Long, TopProductDto> map = new HashMap<>();
        for (SaleEntity sale : sales) {
            for (SaleItemEntity item : sale.getItems()) {
                if (item.getProduct() == null) continue;
                Long productId = item.getProduct().getId();
                String name = item.getProduct().getName();
                TopProductDto agg = map.getOrDefault(productId, TopProductDto.builder()
                        .productId(productId)
                        .productName(name)
                        .quantitySold(0)
                        .revenue(BigDecimal.ZERO)
                        .build());
                long qty = agg.getQuantitySold() + Optional.ofNullable(item.getQuantity()).orElse(0);
                BigDecimal rev = agg.getRevenue().add(Optional.ofNullable(item.getSubtotal()).orElse(BigDecimal.ZERO));
                agg.setQuantitySold(qty);
                agg.setRevenue(rev);
                map.put(productId, agg);
            }
        }

        return map.values().stream()
                .sorted(Comparator.comparingLong(TopProductDto::getQuantitySold).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SellerPerformanceDto> getSellerPerformance(Date start, Date end) {
        List<SaleEntity> sales = saleRepository.findAllWithItemsByDateBetween(start, end);

        Map<Long, SellerPerformanceDto> map = new HashMap<>();
        for (SaleEntity sale : sales) {
            if (sale.getUser() == null) continue;
            Long userId = sale.getUser().getId();
            String email = sale.getUser().getEmail();
            SellerPerformanceDto agg = map.getOrDefault(userId, SellerPerformanceDto.builder()
                    .userId(userId)
                    .userEmail(email)
                    .totalSalesCount(0)
                    .totalItemsSold(0)
                    .totalRevenue(BigDecimal.ZERO)
                    .build());
            agg.setTotalSalesCount(agg.getTotalSalesCount() + 1);
            long items = sale.getItems().stream().mapToLong(i -> Optional.ofNullable(i.getQuantity()).orElse(0)).sum();
            agg.setTotalItemsSold(agg.getTotalItemsSold() + items);
            agg.setTotalRevenue(agg.getTotalRevenue().add(Optional.ofNullable(sale.getTotal()).orElse(BigDecimal.ZERO)));
            map.put(userId, agg);
        }

        return map.values().stream()
                .sorted(Comparator.comparing(SellerPerformanceDto::getTotalRevenue).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailySalesPointDto> getDailySalesSeries(Date start, Date end) {
        List<SaleEntity> sales = saleRepository.findAllWithItemsByDateBetween(start, end);

        Map<LocalDate, DailySalesPointDto> map = new TreeMap<>();
        for (SaleEntity sale : sales) {
            Date saleDate = sale.getDate();
            if (saleDate == null) continue;
            LocalDate day = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DailySalesPointDto point = map.getOrDefault(day, DailySalesPointDto.builder()
                    .date(Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .salesCount(0)
                    .itemsSold(0)
                    .revenue(BigDecimal.ZERO)
                    .build());
            point.setSalesCount(point.getSalesCount() + 1);
            long items = sale.getItems().stream().mapToLong(i -> Optional.ofNullable(i.getQuantity()).orElse(0)).sum();
            point.setItemsSold(point.getItemsSold() + items);
            point.setRevenue(point.getRevenue().add(Optional.ofNullable(sale.getTotal()).orElse(BigDecimal.ZERO)));
            map.put(day, point);
        }

        return new ArrayList<>(map.values());
    }
}


