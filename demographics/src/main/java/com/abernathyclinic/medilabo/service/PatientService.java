package com.abernathyclinic.medilabo.service;

import com.abernathyclinic.medilabo.domain.Patient;
import com.abernathyclinic.medilabo.repository.PatientRepo;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    private final PatientRepo patientRepo;

    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Patient getPatient(String firstName, String lastName) {
        return patientRepo.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }
}

