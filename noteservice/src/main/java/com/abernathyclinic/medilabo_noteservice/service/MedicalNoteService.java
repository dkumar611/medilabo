package com.abernathyclinic.medilabo_noteservice.service;

import com.abernathyclinic.medilabo_noteservice.domain.MedicalNote;
import com.abernathyclinic.medilabo_noteservice.repository.MedicalNoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalNoteService {

    private final MedicalNoteRepository repo;

    public MedicalNoteService(MedicalNoteRepository repo) {
        this.repo = repo;
    }

    public List<MedicalNote> getNotesForPatient(Long patientId) {
        return repo.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public MedicalNote addNote(MedicalNote note) {
        if (note.getCreatedAt() == null) {
            note.setCreatedAt(LocalDateTime.now());
        }
        return repo.save(note);
    }

    // Fetch all notes
    public  List<MedicalNote> getAllNotes() {
        return repo.findAll();
    }

    public MedicalNote getById(String id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteById(String id) {
        repo.deleteById(id);
    }
}

