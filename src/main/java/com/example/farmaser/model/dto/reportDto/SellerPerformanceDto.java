package com.example.farmaser.model.dto.reportDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerPerformanceDto {
    private Long userId;
    private String userEmail;
    private long totalSalesCount;
    private long totalItemsSold;
    private BigDecimal totalRevenue;
}


