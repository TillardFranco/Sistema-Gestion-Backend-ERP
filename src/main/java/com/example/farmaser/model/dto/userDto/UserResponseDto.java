package com.example.farmaser.model.dto.userDto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@ToString
@Builder
public class UserResponseDto implements Serializable {

    private UUID id;
    private String name;
    private String lastname;
    private String email;
    private Timestamp creationDate;
}
