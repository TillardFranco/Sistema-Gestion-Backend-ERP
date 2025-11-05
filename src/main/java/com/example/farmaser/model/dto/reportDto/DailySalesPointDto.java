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
public class DailySalesPointDto {
    private Date date;
    private long salesCount;
    private long itemsSold;
    private BigDecimal revenue;
}


