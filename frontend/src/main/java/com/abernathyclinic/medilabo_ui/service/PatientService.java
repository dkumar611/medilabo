package com.abernathyclinic.medilabo_ui.service;

import com.abernathyclinic.medilabo_ui.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PatientService {

    private final RestTemplate restTemplate;

    @Value("${backend.patient-api-url}")
    private String patientApiUrl;  // Example: http://localhost:8081/patients

    public PatientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // GET ALL
    public List<PatientDTO> getAllPatients() {
        PatientDTO[] list = restTemplate.getForObject(patientApiUrl, PatientDTO[].class);
        return Arrays.asList(list);
    }

    // GET ONE
    public PatientDTO getPatientById(Long id) {
        return restTemplate.getForObject(patientApiUrl + "/" + id, PatientDTO.class);
    }

    // CREATE
    public void createPatient(PatientDTO patient) {
        restTemplate.postForObject(patientApiUrl, patient, PatientDTO.class);
    }

    // UPDATE
    public void updatePatient(Long id, PatientDTO patient) {
        restTemplate.put(patientApiUrl + "/" + id, patient);
    }

    // DELETE
    public void deletePatient(Long id) {
        restTemplate.delete(patientApiUrl + "/" + id);
    }
}

