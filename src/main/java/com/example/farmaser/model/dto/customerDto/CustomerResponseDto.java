package com.example.farmaser.model.dto.customerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {
    private Long id;
    private String dni;
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String city;
    private Boolean active;
    private Date creationDate;
}


