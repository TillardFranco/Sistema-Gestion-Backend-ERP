package com.example.farmaser.model.dto.reportDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesSummaryDto {

    private Date startDate;
    private Date endDate;

    private long totalSalesCount;
    private long totalItemsSold;
    private BigDecimal totalRevenue;
    private BigDecimal totalTax;
    private BigDecimal totalSubtotal;
}


