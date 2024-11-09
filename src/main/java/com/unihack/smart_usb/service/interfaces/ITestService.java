package com.unihack.smart_usb.service.interfaces;

import com.unihack.smart_usb.persistance.model.Test;

import java.util.List;
import java.util.Optional;

public interface ITestService {

    Optional<Test> getTestById(Long id);
    Optional<Test> getTestByTitle(String title);
    List<Test> getTestByTitleQuery(String titleQuery);
    List<Test> getTestByTitleQueryAndProfessorId(String titleQuery, Long professorId);
    Optional<Test> getTestByTitleAndProfessorId(String title, Long professorId);

}
