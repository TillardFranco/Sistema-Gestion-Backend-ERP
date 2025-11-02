package com.example.farmaser.model.dto.stockDto;

import com.example.farmaser.model.entity.MovementType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@Builder
public class StockMovementResponseDto implements Serializable {

    private Long id;
    private Long productId;
    private String productName;
    private String productBarcode;
    private MovementType type;
    private Integer quantity;
    private String reason;
    private Long userId;
    private String userName;
    private Date date;
    private String notes;
}


