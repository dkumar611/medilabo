package com.abernathyclinic.medilabo_diabetes_assessment.client;

import com.abernathyclinic.medilabo_diabetes_assessment.dto.NoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class NotesClient {

    private final WebClient webClient;

    public Flux<NoteDto> getNotes(Integer patientId) {
        return webClient.get()
                .uri("/api/notes/patient/{id}", patientId)
                .retrieve()
                .bodyToFlux(NoteDto.class);
    }
}
