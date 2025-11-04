package com.example.farmaser.model.dto.alertDto;

import com.example.farmaser.model.entity.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponseDto {
    private Long id;
    private AlertType type;
    private String message;
    private Boolean read;
    private Long userId;
    private Long productId;
    private String productName;
    private Date date;
    private Date expirationDate;
}

