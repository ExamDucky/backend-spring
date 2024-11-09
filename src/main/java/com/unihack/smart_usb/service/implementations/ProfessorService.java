package com.unihack.smart_usb.service.implementations;

import com.unihack.smart_usb.persistance.model.Professor;
import com.unihack.smart_usb.persistance.repository.IProfessorRepository;
import com.unihack.smart_usb.service.interfaces.IProfessorService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfessorService implements IProfessorService {
    @NotNull
    IProfessorRepository repository;

    @Override
    public Optional<Professor> getProfessorById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Professor> getProfessorByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Professor saveProfessor(Professor professor) {
        return repository.save(professor);
    }
}
