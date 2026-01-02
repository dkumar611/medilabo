package com.abernathyclinic.medilabo_diabetes_assessment.dto;

public record DiabetesAssessmentDto(
        Integer patientId,
        int age,
        String gender,
        int triggerTermCount,
        String riskLevel
) {}
