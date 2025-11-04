package com.example.farmaser.model.dto.reservationDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDto {

    @NotNull(message = "El customerId es obligatorio")
    private Long customerId;

    @NotNull(message = "El productId es obligatorio")
    private Long productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer quantity;

    @Size(max = 500, message = "Las notas no pueden superar 500 caracteres")
    private String notes;
}

