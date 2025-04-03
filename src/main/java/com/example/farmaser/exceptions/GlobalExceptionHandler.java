package com.example.farmaser.exceptions;


import com.example.farmaser.model.payload.MensajeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MensajeResponse> handleResourceNotFoundException (NotFoundException ex) {
        HttpStatus notFoundResponse = HttpStatus.NOT_FOUND;
        MensajeResponse response = new MensajeResponse(
                notFoundResponse,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(response, notFoundResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MensajeResponse> handleBadRequestException (BadRequestException ex) {
        MensajeResponse response = new MensajeResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeResponse> handleGenericException (Exception ex) {
        MensajeResponse response = new MensajeResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<MensajeResponse> handleDataAccesException (DataAccessException ex) {
        MensajeResponse response = new MensajeResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
