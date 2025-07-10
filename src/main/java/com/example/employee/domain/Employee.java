package com.example.employee.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Employee")
public class Employee {
    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String preferredName;

    private String email;
    private String cellPhone;
    private String alternatePhone;

    private String gender;
    private String ssn;
    private LocalDate dob;

    private LocalDate startDate;
    private LocalDate endDate;

    private String driverLicense;
    private LocalDate driverLicenseExpiration;

    private String houseId;
    private String status;
    private String title;
    private String comment;

    private List<Contact> contact;
    private List<Address> address;
    private List<VisaStatus> visaStatus;
    private List<PersonalDocument> personalDocument;
}