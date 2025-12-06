package com.abernathyclinic.medilabo_ui.controller;

import com.abernathyclinic.medilabo_ui.dto.PatientDTO;
import com.abernathyclinic.medilabo_ui.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientViewController {

    private final PatientService patientService;

    public PatientViewController(PatientService patientService) {
        this.patientService = patientService;
    }

    // LIST PAGE
    @GetMapping("/view")
    public String viewPatients(Model model) {
        model.addAttribute("patients", patientService.getAllPatients());
        model.addAttribute("newPatient", new PatientDTO());
        return "patients";  // patients.html
    }

    // CREATE
    @PostMapping
    public String addPatient(@ModelAttribute PatientDTO patient) {
        patientService.createPatient(patient);
        return "redirect:/patients/view";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients/view";
    }

    // EDIT PAGE
    @GetMapping("/edit/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatientById(id));
        return "edit-patient";  // edit-patient.html
    }

    // UPDATE
    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute PatientDTO updatedPatient) {
        patientService.updatePatient(id, updatedPatient);
        return "redirect:/patients/view";
    }
}
