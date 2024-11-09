package com.unihack.smart_usb.service.implementations;

import com.unihack.smart_usb.persistance.model.Exam;
import com.unihack.smart_usb.persistance.model.ExamAttempt;
import com.unihack.smart_usb.persistance.repository.IExamAttemptRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamAttemptService {

    @NotNull IExamAttemptRepository iExamAttemptRepository;

    public ExamAttempt saveExamAttempt(ExamAttempt examAttempt) {
        return iExamAttemptRepository.save(examAttempt);
    }

    public Optional<ExamAttempt> getExamAttemptById(Long id) {
        return iExamAttemptRepository.findById(id);
    }

    public Optional<ExamAttempt> getExamAttemptByExamIdAndStudentId(Long examId, Long studentId) {
        return iExamAttemptRepository.findByExamIdAndStudentId(examId, studentId);
    }

    public void updateExamAttempt(ExamAttempt examAttempt) {
        iExamAttemptRepository.save(examAttempt);
    }
}
