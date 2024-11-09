package com.unihack.smart_usb.service.implementations;

import com.unihack.smart_usb.api.dto.ExamDTO;
import com.unihack.smart_usb.persistance.model.Exam;
import com.unihack.smart_usb.persistance.repository.IExamRepository;
import com.unihack.smart_usb.service.interfaces.IExamService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamService implements IExamService {

    @NotNull IExamRepository iExamRepository;

    public Exam saveExam(Exam exam) {
        return iExamRepository.save(exam);
    }

    public Optional<Exam> getExamById(Long examId) {
        return iExamRepository.findById(examId);
    }

    public Exam updateExam(Exam exam) {
        return iExamRepository.save(exam);
    }
}
