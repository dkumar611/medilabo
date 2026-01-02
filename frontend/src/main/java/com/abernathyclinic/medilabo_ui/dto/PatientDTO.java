package com.abernathyclinic.medilabo_ui.dto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class PatientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String phone;

    // New fields for diabetes assessment
    private Integer triggerTermCount;
    private String riskLevel;

}
