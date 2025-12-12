package com.abernathyclinic.medilabo_noteservice.controller;

import com.abernathyclinic.medilabo_noteservice.domain.MedicalNote;
import com.abernathyclinic.medilabo_notes.service.MedicalNoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class MedicalNoteController {

    private final MedicalNoteService service;

    public MedicalNoteController(MedicalNoteService service) {
        this.service = service;
    }

    // GET notes for a patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalNote>> getByPatient(@PathVariable Long patientId) {
        List<MedicalNote> notes = service.getNotesForPatient(patientId);
        return ResponseEntity.ok(notes);
    }

    // POST new note (JSON). Example body:
    // { "patientId": 1, "physician":"Dr. X", "note":"text with\nline breaks" }
    @PostMapping
    public ResponseEntity<MedicalNote> addNote(@RequestBody MedicalNote note) {
        if (note.getPatientId() == null || note.getNote() == null) {
            return ResponseEntity.badRequest().build();
        }
        MedicalNote saved = service.addNote(note);
        return ResponseEntity.ok(saved);
    }

    // GET single note
    @GetMapping("/{id}")
    public ResponseEntity<MedicalNote> getById(@PathVariable String id) {
        MedicalNote m = service.getById(id);
        if (m == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(m);
    }

    // DELETE note
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
