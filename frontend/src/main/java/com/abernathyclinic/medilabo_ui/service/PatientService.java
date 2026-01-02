package com.abernathyclinic.medilabo_ui.service;

import com.abernathyclinic.medilabo_ui.dto.PatientDTO;
import com.abernathyclinic.medilabo_ui.dto.DiabetesAssessmentDto; // new DTO for trigger/risk
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientService.class);

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;  // to access cookies

    private final String patientApiUrl;
    private final String diabetesAssessmentApiUrl;

    public PatientService(RestTemplate restTemplate,
                          HttpServletRequest request,
                          @org.springframework.beans.factory.annotation.Value("${backend.patient-api-url}") String patientApiUrl,
                          @org.springframework.beans.factory.annotation.Value("${backend.diabetes-assessment-api-url}") String diabetesAssessmentApiUrl) {
        this.restTemplate = restTemplate;
        this.request = request;
        this.patientApiUrl = patientApiUrl;
        this.diabetesAssessmentApiUrl = diabetesAssessmentApiUrl;
    }

    // Helper: create headers with JWT cookie
    private HttpHeaders createHeadersWithJwt() {
        HttpHeaders headers = new HttpHeaders();
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("JWT-TOKEN".equals(c.getName())) {
                    headers.add(HttpHeaders.COOKIE, "JWT-TOKEN=" + c.getValue());
                    break;
                }
            }
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // GET ALL patients + attach trigger/risk
    public List<PatientDTO> getAllPatients() {
        try {
            HttpEntity<String> entity = new HttpEntity<>(createHeadersWithJwt());
            ResponseEntity<PatientDTO[]> response = restTemplate.exchange(
                    patientApiUrl,
                    HttpMethod.GET,
                    entity,
                    PatientDTO[].class
            );
            PatientDTO[] list = response.getBody();
            if (list == null) return Collections.emptyList();

            // For each patient, fetch assessment
            for (PatientDTO p : list) {
                attachDiabetesAssessment(p);
            }

            return Arrays.asList(list);
        } catch (RestClientException ex) {
            log.warn("Failed to fetch patients from backend: {}", ex.toString());
            return Collections.emptyList();
        }
    }

    // GET ONE patient + attach trigger/risk
    public PatientDTO getPatientById(Long id) {
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithJwt());
        ResponseEntity<PatientDTO> response = restTemplate.exchange(
                patientApiUrl + "/" + id,
                HttpMethod.GET,
                entity,
                PatientDTO.class
        );
        PatientDTO patient = response.getBody();
        if (patient != null) attachDiabetesAssessment(patient);
        return patient;
    }

    // Helper method to call diabetes assessment API and set values
    public void attachDiabetesAssessment(PatientDTO patient) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(createHeadersWithJwt());
            ResponseEntity<DiabetesAssessmentDto> assessmentResponse = restTemplate.exchange(
                    diabetesAssessmentApiUrl + "/" + patient.getId(),
                    HttpMethod.GET,
                    entity,
                    DiabetesAssessmentDto.class
            );
            DiabetesAssessmentDto assessment = assessmentResponse.getBody();
            if (assessment != null) {
                patient.setTriggerTermCount(assessment.getTriggerTermCount());
                patient.setRiskLevel(assessment.getRiskLevel());
            } else {
                patient.setTriggerTermCount(0);
                patient.setRiskLevel("None");
            }
        } catch (RestClientException ex) {
            log.warn("Failed to fetch diabetes assessment for patient {}: {}", patient.getId(), ex.toString());
            patient.setTriggerTermCount(0);
            patient.setRiskLevel("None");
        }
    }

    // CREATE
    public void createPatient(PatientDTO patient) {
        HttpEntity<PatientDTO> entity = new HttpEntity<>(patient, createHeadersWithJwt());
        restTemplate.exchange(patientApiUrl, HttpMethod.POST, entity, PatientDTO.class);
    }

    // UPDATE
    public void updatePatient(Long id, PatientDTO patient) {
        HttpEntity<PatientDTO> entity = new HttpEntity<>(patient, createHeadersWithJwt());
        restTemplate.exchange(patientApiUrl + "/" + id, HttpMethod.PUT, entity, Void.class);
    }

    // DELETE
    public void deletePatient(Long id) {
        HttpEntity<String> entity = new HttpEntity<>(createHeadersWithJwt());
        restTemplate.exchange(patientApiUrl + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }
}
