package com.abernathyclinic.medilabo_ui.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalNoteDto {

    private String id;
    private Long patientId;
    private String physician;
    private String note;
    private LocalDateTime createdAt;
}

