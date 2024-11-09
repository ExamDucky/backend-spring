package com.unihack.smart_usb.persistance.repository;

import com.unihack.smart_usb.persistance.model.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    @Query(value = "SELECT ea FROM ExamAttempt ea WHERE ea.exam.id=?1 AND ea.student.id=?2")
    Optional<ExamAttempt> findByExamIdAndStudentId(Long examId, Long studentId);

}
