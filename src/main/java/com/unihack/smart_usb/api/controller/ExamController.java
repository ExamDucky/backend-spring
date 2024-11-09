package com.unihack.smart_usb.api.controller;

import com.unihack.smart_usb.api.dto.ExamAttemptDTO;
import com.unihack.smart_usb.api.dto.ExamDTO;
import com.unihack.smart_usb.api.dto.TestDTO;
import com.unihack.smart_usb.client.models.TestFileType;
import com.unihack.smart_usb.facade.ExamFacade;
import com.unihack.smart_usb.persistance.model.Professor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/exam")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class ExamController {

    private final ExamFacade examFacade;

    @Operation(summary = "Create new exam", description = "Method for creating/starting new exam by the professor")
    @PostMapping("/start")
    public ResponseEntity<ExamDTO> createNewExam(Authentication authentication, @RequestBody ExamDTO examDTO) {
        Professor professor = (Professor) authentication.getPrincipal();
        log.info("Starting exam for professor with id: " + professor.getId());
        return ResponseEntity.ok(examFacade.createExam(examDTO, professor.getId()));
    }

    @Operation(summary = "End exam", description = "Method for ending exam by the professor")
    @PutMapping("/{id}/end")
    public ResponseEntity<ExamDTO> endExam(Authentication authentication, @PathVariable @NotNull Long id) {
        Professor professor = (Professor) authentication.getPrincipal();
        log.info("Ending exam for professor with id: " + professor.getId());
        return ResponseEntity.ok(examFacade.endExam(id, professor.getId()));
    }

    @Operation(summary = "Create exam attempt", description = "Method for creating exam attempt by student's usb")
    @PostMapping("/{id}/exam-attempt/create")
    public ResponseEntity<ExamAttemptDTO> createExamAttempt(Authentication authentication,
                                                            @PathVariable @NotNull Long id,
                                                            @RequestBody ExamAttemptDTO examAttemptDTO) {
        log.info("Creating exam attempt for exam with id: " + id);
        return ResponseEntity.ok(examFacade.createExamAttempt(id, examAttemptDTO));
    }

    @Operation(summary = "Create exam attempt", description = "Method for creating exam attempt by student's usb")
    @PutMapping(value = "/{id}/exam-attempt/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> submitTest(Authentication authentication,
                                              @PathVariable @NotNull Long id,
                                              @RequestParam("file") MultipartFile file,
                                              @NotNull @RequestParam("filename") String filename,
                                              @RequestParam String studentIdentification,
                                              @RequestParam Long examAttemptId,
                                              @NotNull @RequestParam("testFileType") TestFileType testFileType) {
        log.info("Creating exam attempt for exam with id: " + id);
        return ResponseEntity.ok(examFacade.submitExam(id, examAttemptId, file, filename, studentIdentification, testFileType));
    }
}
