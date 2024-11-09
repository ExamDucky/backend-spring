package com.unihack.smart_usb.persistance.repository;

import com.unihack.smart_usb.persistance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
}
