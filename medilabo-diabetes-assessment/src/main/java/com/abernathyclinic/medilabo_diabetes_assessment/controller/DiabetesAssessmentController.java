package com.abernathyclinic.medilabo_diabetes_assessment.controller;

import com.abernathyclinic.medilabo_diabetes_assessment.dto.DiabetesAssessmentDto;
import com.abernathyclinic.medilabo_diabetes_assessment.service.DiabetesAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class DiabetesAssessmentController {

    private final DiabetesAssessmentService service;

    @GetMapping("/diabetes/{patientId}")
    public Mono<DiabetesAssessmentDto> assess(
            @PathVariable Integer patientId) {
        return service.assess(patientId);
    }
}
