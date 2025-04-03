package com.example.farmaser.model.payload;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
public class MensajeResponse implements Serializable {

    private final HttpStatus httpStatus;
    private final String mensaje;
    private final ZonedDateTime timestamp;

}
