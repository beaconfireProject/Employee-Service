package com.example.employee.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.example.employee.dto.DtoError;

@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<DtoError> handleException(Exception ex) {
        DtoError body = DtoError.builder()
                .success(false)
                .status(400)
                .error("Exception of class Exception caught")
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(400).body(body);
    }
}