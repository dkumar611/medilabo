package com.abernathyclinic.medilabo_diabetes_assessment.dto;

public record NoteDto(
        String id,
        Integer patientId,
        String note
) {}

