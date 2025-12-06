package com.abernathyclinic.medilabo.controller;
import com.abernathyclinic.medilabo.service.PatientService;
import com.abernathyclinic.medilabo.domain.Patient;
import com.abernathyclinic.medilabo.repository.PatientRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientRepo patientRepo;
    private final PatientService patientService;

    public PatientController(PatientRepo patientRepo, PatientService patientService) {
        this.patientRepo = patientRepo;
        this.patientService = patientService;
    }


    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    // GET /patients/search?firstName=John&lastName=Doe
    @GetMapping("/search")
    public ResponseEntity<Patient> getPatientByName(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        Patient patient = patientService.getPatient(firstName, lastName);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable long id) {
        return patientRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepo.save(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient updatedPatient) {

        Patient updated = patientService.updatePatient(id, updatedPatient);
        return ResponseEntity.ok(updated);
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
