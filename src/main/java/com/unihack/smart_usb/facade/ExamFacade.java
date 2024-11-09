package com.unihack.smart_usb.facade;

import com.unihack.smart_usb.api.dto.ExamAttemptDTO;
import com.unihack.smart_usb.api.dto.ExamDTO;
import com.unihack.smart_usb.api.dto.TestDTO;
import com.unihack.smart_usb.client.BlobStorageTestsClient;
import com.unihack.smart_usb.client.models.TestFileType;
import com.unihack.smart_usb.exception.auth.EntityDoesNotExistException;
import com.unihack.smart_usb.exception.auth.UserDoesNotOwnEntityException;
import com.unihack.smart_usb.persistance.model.*;
import com.unihack.smart_usb.service.implementations.*;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamFacade {

    private final TestService testService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final ExamService examService;
    private final ExamAttemptService examAttemptService;

    @Resource
    private BlobStorageTestsClient examBlobStorageClient;

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

    public ExamAttemptDTO createExamAttempt(Long examId, ExamAttemptDTO examAttemptDTO) {
        Optional<Exam> examOptional = examService.getExamById(examId);
        if (!examOptional.isPresent()) {
            throw new EntityDoesNotExistException("An exam with the given id does not exist.");
        }

        Exam exam = examOptional.get();

        Optional<Student> studentOptional = studentService.getStudentByStudentIdentification(examAttemptDTO.getStudentId());
        if (!studentOptional.isPresent()) {
            throw new EntityDoesNotExistException("An student with the given id does not exist.");
        }

        Student student = studentOptional.get();

        ExamAttempt examAttempt = ExamAttempt.builder()
                .student(student)
                .macAddress(examAttemptDTO.getMacAddress())
                .exam(exam)
                .build();

        ExamAttempt savedExamAttempt = examAttemptService.saveExamAttempt(examAttempt);
        return ExamAttemptDTO.builder()
                .id(savedExamAttempt.getId())
                .examId(exam.getId())
                .studentId(student.getStudentId())
                .macAddress(savedExamAttempt.getMacAddress())
                .build();
    }

    public Boolean submitExam(Long examId, Long examAttemptId, MultipartFile file, String filename, String studentIdentification, TestFileType testFileType) {
        Optional<Exam> examOptional = examService.getExamById(examId);
        if (!examOptional.isPresent()) {
            throw new EntityDoesNotExistException("An exam with the given id does not exist.");
        }

        Optional<Student> studentOptional = studentService.getStudentByStudentIdentification(studentIdentification);
        if (!studentOptional.isPresent()) {
            throw new EntityDoesNotExistException("A student with the given id does not exist.");
        }

        Student student = studentOptional.get();

        ExamAttempt examAttempt;

        if (examAttemptId != null) {
            Optional<ExamAttempt> examAttemptOptional = examAttemptService.getExamAttemptById(examAttemptId);
            if (examAttemptOptional.isPresent()) {
                examAttempt = examAttemptOptional.get();
                try {
                    if (examAttempt.getSubmittedFileName() == null) {
                        examBlobStorageClient.createExamSubmission(student.getId(), examAttempt.getId(), file, filename + examAttemptId.toString(), testFileType);
                        examAttempt.setSubmittedFileName(filename);
                        examAttemptService.updateExamAttempt(examAttempt);
                        return true;
                    } else {
                        throw new EntityDoesNotExistException("The student already submitted the test.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        } else {
            Optional<ExamAttempt> examAttemptOptional2 = examAttemptService.getExamAttemptByExamIdAndStudentId(examId, student.getId());
            if (examAttemptOptional2.isPresent()) {
                examAttempt = examAttemptOptional2.get();
                try {
                    if (examAttempt.getSubmittedFileName() == null) {
                        examBlobStorageClient.createExamSubmission(student.getId(), examAttempt.getId(), file, filename, testFileType);
                        examAttemptService.updateExamAttempt(examAttempt);
                        return true;
                    } else {
                        throw new EntityDoesNotExistException("The student already submitted the test.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        return false;
    }

}
