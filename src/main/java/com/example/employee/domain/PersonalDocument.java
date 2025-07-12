package com.example.employee.domain;

import lombok.Data;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;

@Data
public class PersonalDocument {
    @Id
    private String id;
    private String path;
    private String title;
    private String comment;
    private LocalDate createDate;
}
