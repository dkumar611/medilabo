package com.abernathyclinic.medilabo_ui.controller;

import com.abernathyclinic.medilabo_ui.dto.MedicalNoteDto;
import com.abernathyclinic.medilabo_ui.service.NotesService;
import com.abernathyclinic.medilabo_ui.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class NotesViewController {

    private final NotesService notesService;
    private final PatientService patientService;

    public NotesViewController(NotesService notesService,
                               PatientService patientService) {
        this.notesService = notesService;
        this.patientService = patientService;
    }


    // VIEW NOTES FOR PATIENT
    @GetMapping("/{patientId}/notes")
    public String viewPatientNotes(@PathVariable Long patientId, Model model) {

        model.addAttribute("patient",
                patientService.getPatientById(patientId));

        model.addAttribute("notes",
                notesService.getNotesByPatient(patientId));

        MedicalNoteDto newNote = new MedicalNoteDto();
        newNote.setPatientId(patientId);

        // ADD THIS:

        model.addAttribute("newNote", newNote);

        return "patient-notes"; // patient-notes.html
    }

    // ADD NOTE
    @PostMapping("/{patientId}/notes")
    public String addNote(@PathVariable Long patientId,
                          @ModelAttribute MedicalNoteDto newNote) {

        newNote.setPatientId(patientId);
        notesService.addNote(newNote);

        return "redirect:/patients/" + patientId + "/notes";
    }
}

