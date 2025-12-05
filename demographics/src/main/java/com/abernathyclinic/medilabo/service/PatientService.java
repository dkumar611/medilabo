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


    public Patient updatePatient(Long id, Patient updated) {
        Patient existing = patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setGender(updated.getGender());
        existing.setAddress(updated.getAddress());
        existing.setPhone(updated.getPhone());

        return patientRepo.save(existing);
    }

}

