package com.abernathyclinic.medilabo_noteservice.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "medical_notes")
public class MedicalNote {
    @Id
    private String id;

    // reference to patient (SQL patient id)
    private Long patientId;

    // physician name or identifier
    private String physician;

    // full text note. preserve formatting (line breaks).
    private String note;

    // creation timestamp
    private LocalDateTime createdAt;
}
