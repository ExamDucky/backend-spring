package com.unihack.smart_usb.persistance.repository;

import com.unihack.smart_usb.persistance.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProfessorRepository extends JpaRepository<Professor, Long> {

    Optional<Professor> findByEmail(String email);


}
