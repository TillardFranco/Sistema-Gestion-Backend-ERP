package com.example.farmaser.model.dto.customerDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDto {

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 20, message = "El DNI no puede superar 20 caracteres")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar 100 caracteres")
    private String lastname;

    @Email(message = "El email no tiene un formato válido")
    @Size(max = 150, message = "El email no puede superar 150 caracteres")
    private String email;

    @Size(max = 50, message = "El teléfono no puede superar 50 caracteres")
    private String phone;

    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    private String address;

    @Size(max = 100, message = "La ciudad no puede superar 100 caracteres")
    private String city;

    private Boolean active;
}


