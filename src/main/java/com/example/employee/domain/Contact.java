package com.example.employee.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Contact {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String cellPhone;
    private String alternatePhone;
    private String email;
    private String relationship;
    private String type;
}

