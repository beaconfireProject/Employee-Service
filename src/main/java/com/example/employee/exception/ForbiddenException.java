package com.example.employee.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String detail) {
        super("Resource is forbidden to this user: " + detail);
    }
}
