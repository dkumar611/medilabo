package com.abernathyclinic.medilabo_diabetes_assessment.dto;

import java.time.LocalDate;

public record PatientDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth, // must match JSON
        String gender
) {}


