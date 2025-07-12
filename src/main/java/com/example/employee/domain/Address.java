package com.example.employee.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Address {
    @Id
    private String id;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}

