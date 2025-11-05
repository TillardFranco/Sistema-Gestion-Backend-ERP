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
public class TopProductDto {
    private Long productId;
    private String productName;
    private long quantitySold;
    private BigDecimal revenue;
}


