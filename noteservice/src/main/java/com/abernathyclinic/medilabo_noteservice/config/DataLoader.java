package com.abernathyclinic.medilabo_noteservice.config;

import com.abernathyclinic.medilabo_noteservice.domain.MedicalNote;
import com.abernathyclinic.medilabo_noteservice.repository.MedicalNoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadTestData(MedicalNoteRepository repo) {
        return args -> {
            // Only populate if empty (safe for iterative dev)
            if (repo.count() == 0) {
                List<MedicalNote> sample = List.of(
                        new MedicalNote(null, 1L, "Dr. Adams",
                                "Patient A — initial observation:\n- Fasting glucose borderline\n- Advised diet change and re-test in 2 weeks.",
                                LocalDateTime.now().minusDays(10)),

                        new MedicalNote(null, 2L, "Dr. Lee",
                                "Patient B — notes:\nPatient reports increased thirst and fatigue.\nOrdered HbA1c and recommended hydration.",
                                LocalDateTime.now().minusDays(5)),

                        new MedicalNote(null, 3L, "Dr. Garcia",
                                "Patient C — follow-up:\nSymptoms improved after exercise and dietary advice.\nContinue current plan.",
                                LocalDateTime.now().minusDays(2)),

                        new MedicalNote(null, 4L, "Dr. Taylor",
                                "Patient D — emergency note:\nHigh blood sugar detected; advised immediate clinic visit.\nPrescribed short course and follow-up.",
                                LocalDateTime.now().minusHours(48))
                );

                repo.saveAll(sample);
                System.out.println("Medilabo Notes: seeded " + repo.count() + " documents.");
            } else {
                System.out.println("Medilabo Notes: DB already contains " + repo.count() + " documents; skipping seed.");
            }
        };
    }
}
