package com.example.farmaser.model.dto.productDto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
@Builder
public class ProductRequestDto implements Serializable {

    private String barcode;
    private String name;
    private String description;
    private BigDecimal price;
    private String image_url;
    private Integer stock;
    private Date expiration_date;

}
