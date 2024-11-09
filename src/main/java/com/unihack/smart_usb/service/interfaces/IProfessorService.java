package com.unihack.smart_usb.service.interfaces;

import com.unihack.smart_usb.persistance.model.Professor;

import java.util.Optional;

public interface IProfessorService {
    Optional<Professor> getProfessorById(Long id);

    Optional<Professor> getProfessorByEmail(String email);

    Professor saveProfessor(Professor professor);

}
