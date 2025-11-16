package com.abernathyclinic.medilabo.controller;

import com.abernathyclinic.medilabo.domain.Patient;
import com.abernathyclinic.medilabo.repository.PatientRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepo patientRepo;

    public PatientController(PatientRepo service) {
        this.patientRepo = service;
    }


    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepo.save(patient);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientRepo.deleteById(id);
    }

    // Delete patients by name
    @DeleteMapping("/deleteByLastName")
    public String deletePatientsByName(@RequestParam String lastName) {
        patientRepo.deleteByLastName(lastName);
        return "Deleted all patients with name: " + lastName;
    }

}
