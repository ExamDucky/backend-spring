package com.unihack.smart_usb.facade;

import com.unihack.smart_usb.api.dto.ExamDTO;
import com.unihack.smart_usb.api.dto.TestDTO;
import com.unihack.smart_usb.exception.auth.EntityDoesNotExistException;
import com.unihack.smart_usb.exception.auth.UserDoesNotOwnEntityException;
import com.unihack.smart_usb.persistance.model.Exam;
import com.unihack.smart_usb.persistance.model.Professor;
import com.unihack.smart_usb.persistance.model.Test;
import com.unihack.smart_usb.service.implementations.ExamService;
import com.unihack.smart_usb.service.implementations.ProfessorService;
import com.unihack.smart_usb.service.implementations.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamFacade {

    private final TestService testService;
    private final ProfessorService professorService;

    private final ExamService examService;

    public ExamDTO createExam(ExamDTO examDTO, Long professorId) {
        Optional<Professor> professorOptional = professorService.getProfessorById(professorId);
        Professor professor;
        if (!professorOptional.isPresent()) {
            throw new EntityDoesNotExistException("Professor with the given id does not exist.");
        }

        Optional<Test> testOptional = testService.getTestById(examDTO.getTestId());
        if (!testOptional.isPresent()) {
            throw new EntityDoesNotExistException("The test with the given id does not exist.");
        }

        Test test = testOptional.get();
        professor = professorOptional.get();

        if (test.getProfessor().getId() != professorId) {
            throw new UserDoesNotOwnEntityException("The selected test does not belong to this professor");
        }

        Exam exam = Exam.builder()
                .created(ZonedDateTime.now())
                .location(examDTO.getClassroomName())
                .test(test)
                .build();

        Exam savedExam = examService.saveExam(exam);
        TestDTO testDTO = TestDTO.builder()
                .id(test.getId())
                .title(test.getTitle())
                .duration(test.getDuration())
                .description(test.getDescription())
                .groupOneTestFileUri(test.getGroupOneTestFileName())
                .groupTwoTestFileUri(test.getGroupTwoTestFileName())
                .professorId(test.getProfessor().getId())
                .blacklistProcessesFileName(test.getBlacklistProcessesFileName())
                .build();
        return ExamDTO.builder()
                .id(savedExam.getId())
                .examStarted(savedExam.getCreated())
                .classroomName(savedExam.getLocation())
                .testDTO(testDTO)
                .build();
    }

    public ExamDTO endExam(Long examId, Long professorId) {

        Optional<Professor> professorOptional = professorService.getProfessorById(professorId);
        Professor professor;
        if (!professorOptional.isPresent()) {
            throw new EntityDoesNotExistException("Professor with the given id does not exist.");
        }

        Optional<Exam> examOptional = examService.getExamById(examId);
        if (!examOptional.isPresent()) {
            throw new EntityDoesNotExistException("An exam with the given id does not exist.");
        }

        Exam exam = examOptional.get();

        exam.setEnded(ZonedDateTime.now());

        Exam savedExam = examService.updateExam(exam);
        return ExamDTO.builder()
                .id(savedExam.getId())
                .examStarted(savedExam.getCreated())
                .examEnded(savedExam.getEnded())
                .classroomName(savedExam.getLocation())
                .build();

    }
}
