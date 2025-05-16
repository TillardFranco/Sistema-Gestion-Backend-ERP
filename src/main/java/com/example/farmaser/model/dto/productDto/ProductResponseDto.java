package com.example.farmaser.model.dto.productDto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
@Builder
public class ProductResponseDto implements Serializable {

    private String barcode;
    private String name;
    private String description;
    private BigDecimal price;
    private String image_url;
    private Integer stock;

}
