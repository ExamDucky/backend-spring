package com.unihack.smart_usb.api.controller;

import com.unihack.smart_usb.api.dto.ExamDTO;
import com.unihack.smart_usb.api.dto.TestDTO;
import com.unihack.smart_usb.facade.ExamFacade;
import com.unihack.smart_usb.persistance.model.Professor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}
