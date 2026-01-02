package com.abernathyclinic.medilabo_diabetes_assessment.service;

import com.abernathyclinic.medilabo_diabetes_assessment.client.NotesClient;
import com.abernathyclinic.medilabo_diabetes_assessment.client.PatientClient;
import com.abernathyclinic.medilabo_diabetes_assessment.dto.DiabetesAssessmentDto;
import com.abernathyclinic.medilabo_diabetes_assessment.dto.NoteDto;
import com.abernathyclinic.medilabo_diabetes_assessment.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiabetesAssessmentService {

    private final PatientClient patientClient;
    private final NotesClient notesClient;

    private static final List<String> TRIGGERS = List.of(
            "Hemoglobin A1C", "Microalbumin", "Height", "Weight",
            "Smoking", "Abnormal", "Cholesterol", "Dizziness",
            "Relapse", "Reaction", "Antibody"
    );

    public Mono<DiabetesAssessmentDto> assess(Integer patientId) {

        Mono<PatientDto> patientMono = patientClient.getPatient(patientId);
        Mono<List<NoteDto>> notesMono = notesClient.getNotes(patientId).collectList();

        return Mono.zip(patientMono, notesMono)
                .map(tuple -> {
                    PatientDto patient = tuple.getT1();
                    List<NoteDto> notes = tuple.getT2();

                    int age = Period.between(patient.dateOfBirth(), LocalDate.now()).getYears();

                    int triggerCount = countTriggers(notes);
                    String risk = calculateRisk(age, patient.gender(), triggerCount);

                    return new DiabetesAssessmentDto(
                            patientId,
                            age,
                            patient.gender(),
                            triggerCount,
                            risk
                    );
                });
    }

    private int countTriggers(List<NoteDto> notes) {
        String combined = notes.stream()
                .map(NoteDto::note)
                .collect(Collectors.joining(" "))
                .toLowerCase();

        return (int) TRIGGERS.stream()
                .filter(t -> combined.contains(t.toLowerCase()))
                .count();
    }

    private String calculateRisk(int age, String gender, int count) {

        if (age > 30) {
            if (count >= 8) return "Early Onset";
            if (count >= 6) return "In Danger";
            if (count >= 2) return "Borderline";
            return "None";
        }

        if ("M".equalsIgnoreCase(gender)) {
            if (count >= 5) return "Early Onset";
            if (count >= 3) return "In Danger";
        } else {
            if (count >= 6) return "Early Onset";
            if (count >= 4) return "In Danger";
        }

        return "None";
    }
}
