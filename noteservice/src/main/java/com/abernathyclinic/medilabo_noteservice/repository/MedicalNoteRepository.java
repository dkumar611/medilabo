package com.abernathyclinic.medilabo_noteservice.repository;

import com.abernathyclinic.medilabo_noteservice.domain.MedicalNote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicalNoteRepository extends MongoRepository<MedicalNote, String> {
    // get notes for a patient, newest first
    List<MedicalNote> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
