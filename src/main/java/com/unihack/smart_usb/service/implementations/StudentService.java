package com.unihack.smart_usb.service.implementations;

import com.unihack.smart_usb.persistance.model.Student;
import com.unihack.smart_usb.persistance.repository.IStudentRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentService {

    @NotNull IStudentRepository iStudentRepository;

    public Optional<Student> getStudentByStudentIdentification(String studentId) {
        Optional<Student> student = iStudentRepository.findByStudentId(studentId);
        return student;
    }
}
