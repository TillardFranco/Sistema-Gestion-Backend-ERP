package com.example.farmaser.model.dto.userDto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@Builder
public class UserRequestDto implements Serializable {

    private String name;
    private String lastname;
    private String email;
    private String password;
}
