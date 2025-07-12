package com.example.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DtoError {
    private boolean success;
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
}
