package com.abernathyclinic.medilabo_ui.service;

import com.abernathyclinic.medilabo_ui.dto.MedicalNoteDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class NotesService {

    private final RestTemplate restTemplate;

    @Value("${gateway.url}")
    private String gatewayUrl;

    public NotesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MedicalNoteDto> getNotesByPatient(Long patientId) {
        String url = gatewayUrl + "/api/notes/patient/" + patientId;
        MedicalNoteDto[] notes =
                restTemplate.getForObject(url, MedicalNoteDto[].class);
        return Arrays.asList(notes);
    }

    public void addNote(MedicalNoteDto note) {
        String url = gatewayUrl + "/api/notes";
        restTemplate.postForObject(url, note, MedicalNoteDto.class);
    }
}

