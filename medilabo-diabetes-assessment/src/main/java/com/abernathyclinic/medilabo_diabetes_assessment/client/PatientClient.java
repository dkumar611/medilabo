package com.abernathyclinic.medilabo_diabetes_assessment.client;

import com.abernathyclinic.medilabo_diabetes_assessment.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PatientClient {

    private final WebClient webClient;

    public Mono<PatientDto> getPatient(Integer patientId) {
        return webClient.get()
                .uri("/api/patients/{id}", patientId)
                .retrieve()
                .bodyToMono(PatientDto.class);
    }
}
