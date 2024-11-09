package com.unihack.smart_usb.persistance.repository;

import com.unihack.smart_usb.persistance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IStudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.studentId=?1")
    Optional<Student> findByStudentId(String studentId);
}
