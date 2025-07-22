package com.sharp.sharpshap.error;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Value
public class ErrorResponse {
    private final String message;
    private final int status;
    private final String timestamp;

    public static ResponseEntity<Object> error(RuntimeException e , HttpStatus status){
        ErrorResponse error = new ErrorResponse(
                e.getMessage(),
                status.value(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(status).body(error);
    }
}