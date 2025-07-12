package com.example.employee.domain;

import lombok.Data;

import org.springframework.data.annotation.Id;
import java.time.LocalDate;

@Data
public class VisaStatus {
    @Id
    private String id;
    private String visaType;
    private Boolean activeFlag;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastModificationDate;
}

