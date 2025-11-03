package com.example.farmaser.model.dto.saleDto;

import com.example.farmaser.model.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleRequestDto {

    @NotNull(message = "El customerId es obligatorio")
    private Long customerId;

    @NotNull(message = "El método de pago es obligatorio")
    private PaymentMethod paymentMethod;

    @NotEmpty(message = "Debe incluir al menos un item")
    @Valid
    private List<SaleItemRequestDto> items;
}

