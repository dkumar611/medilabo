package com.abernathyclinic.medilabo.repository;

import com.abernathyclinic.medilabo.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;  // <-- Correct import

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {

    @Transactional
    void deleteByLastName(String lastName);

    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

}


