package com.example.farmaser.model.dto.reservationDto;

import com.example.farmaser.model.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDto {
    private Long id;
    private String reservationNumber;
    private Long customerId;
    private String customerName;
    private Long productId;
    private String productName;
    private Integer quantity;
    private ReservationStatus status;
    private Date reservationDate;
    private Date expirationDate;
    private String notes;
}

