package com.example.employee.dto;

import com.example.employee.domain.Address;
import com.example.employee.domain.Contact;
import com.example.employee.domain.PersonalDocument;
import com.example.employee.domain.VisaStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

// currently unused, maybe for validation checks

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequest {
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
