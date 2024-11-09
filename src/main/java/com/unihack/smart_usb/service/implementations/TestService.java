package com.unihack.smart_usb.service.implementations;

import com.unihack.smart_usb.persistance.model.Test;
import com.unihack.smart_usb.persistance.repository.ITestRepository;
import com.unihack.smart_usb.service.interfaces.ITestService;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestService implements ITestService {

    @NotNull ITestRepository repository;


    @Override
    public Optional<Test> getTestById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Test> getTestByTitle(String title) {
        return repository.findByTitle(title);
    }

    @Override
    public List<Test> getTestByTitleQuery(String titleQuery) {
        return repository.findByTitleQuery(titleQuery);
    }

    @Override
    public List<Test> getTestByTitleQueryAndProfessorId(String titleQuery, Long professorId) {
        return repository.findByTitleQueryAndProfessorId(titleQuery, professorId);
    }

    @Override
    public Optional<Test> getTestByTitleAndProfessorId(String title, Long professorId) {
        return repository.findByTitleAndProfessorId(title, professorId);
    }

    public Test saveTest(Test testToSave) {
        return repository.save(testToSave);
    }
}
