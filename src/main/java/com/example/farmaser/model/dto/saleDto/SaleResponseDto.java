package com.example.farmaser.model.dto.saleDto;

import com.example.farmaser.model.entity.PaymentMethod;
import com.example.farmaser.model.entity.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponseDto {
    private Long id;
    private String saleNumber;
    private Long customerId;
    private String customerName;
    private Long userId;
    private Date date;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentMethod paymentMethod;
    private SaleStatus status;
    private List<SaleItemResponseDto> items;
}

