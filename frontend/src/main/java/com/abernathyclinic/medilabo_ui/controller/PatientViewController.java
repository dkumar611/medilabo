package com.abernathyclinic.medilabo_ui.controller;

import com.abernathyclinic.medilabo_ui.dto.PatientDTO;
import com.abernathyclinic.medilabo_ui.service.PatientService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Controller
@RequestMapping("/patients")
public class PatientViewController {

    private final PatientService patientService;

    public PatientViewController(PatientService patientService) {
        this.patientService = patientService;
    }

    // LIST PAGE
    @GetMapping("/view")
    public String viewPatients(Model model, HttpServletRequest request) {
        // If user is not authenticated (no JWT cookie), redirect the user to the gateway login page
        Cookie[] cookies = request.getCookies();
        boolean hasJwt = false;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("JWT-TOKEN".equals(c.getName())) {
                    hasJwt = true;
                    break;
                }
            }
        }

        if (!hasJwt) {
            // Redirect to gateway's login page (gateway runs on :8080)
            return "redirect:http://localhost:8080/login";
        }

        try {
            List<PatientDTO> patients = patientService.getAllPatients();

            // Fetch and attach diabetes assessment info for each patient
            for (PatientDTO patient : patients) {
                patientService.attachDiabetesAssessment(patient);
            }

            model.addAttribute("patients", patients);

        } catch (RestClientException ex) {
            // If backend returned HTML (e.g. login page) or other error, redirect to gateway login
            return "redirect:http://localhost:8080/login";
        }

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
        PatientDTO patient = patientService.getPatientById(id);

        // Also attach diabetes assessment info for edit page if needed
        patientService.attachDiabetesAssessment(patient);

        model.addAttribute("patient", patient);
        return "edit-patient";  // edit-patient.html
    }

    // UPDATE
    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute PatientDTO updatedPatient) {
        patientService.updatePatient(id, updatedPatient);
        return "redirect:/patients/view";
    }
}
