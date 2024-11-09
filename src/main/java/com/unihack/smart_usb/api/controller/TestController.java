package com.unihack.smart_usb.api.controller;

import com.unihack.smart_usb.api.dto.TestDTO;
import com.unihack.smart_usb.client.models.TestFileType;
import com.unihack.smart_usb.facade.TestFacade;
import com.unihack.smart_usb.persistance.model.Professor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Slf4j
public class TestController {
    private final TestFacade testFacade;


    @Operation(summary = "Get test by id", description = "Method for getting a test owned by the professor, by the given id")
    @GetMapping("/{id}")
    public ResponseEntity<TestDTO> getTestById(Authentication authentication, @NotNull @PathVariable Long id) {
        Professor professor = (Professor) authentication.getPrincipal();
        log.info("Get test by id for professor with id: " + professor.getId());
        return ResponseEntity.ok(testFacade.getTestById(id, professor));
    }

//    @Operation(summary = "Get test by title", description = "Method for getting a test owned by the professor, by the given title")
//    @GetMapping()
//    public ResponseEntity<TestDto> getTestByTitle(Authentication authentication, @RequestParam String title) {
//        Professor professor = (Professor) authentication.getPrincipal();
//        log.info("Get test by title for professor with id: " + professor.getId());
//        return ResponseEntity.ok(testFacade.getTestByTitle(title, professor));
//    }

    @Operation(summary = "Search tests by title", description = "Method for getting the tests owned by the professor, where the titles start with the given query")
    @GetMapping("/search")
    public ResponseEntity<List<TestDTO>> getTestsByTitleQuery(Authentication authentication, @RequestParam String titleQuery) {
        Professor professor = (Professor) authentication.getPrincipal();
        log.info("Get tests by title query for professor with id: " + professor.getId());
        return ResponseEntity.ok(testFacade.getTestByTitleQuery(titleQuery, professor));
    }

    @Operation(summary = "Search tests by title", description = "Method for getting the tests owned by the professor, where the titles start with the given query")
    @PostMapping("/create")
    public ResponseEntity<TestDTO> createNewTest(Authentication authentication, @RequestBody TestDTO testDto) {
        Professor professor = (Professor) authentication.getPrincipal();
        log.info("Create new test by the professor with id: " + professor.getId());
        return ResponseEntity.ok(testFacade.createNewTest(testDto, professor.getId()));
    }

    @Operation(summary = "Import test file", description = "Method for importing test file by the professor")
    @PutMapping(value = "/{id}/import-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Boolean> uploadTestFile(Authentication authentication,
                                                  @NotNull @PathVariable Long id,
                                                  @RequestParam("file") MultipartFile file,
                                                  @NotNull @RequestParam("filename") String filename,
                                                  @NotNull @RequestParam("testFileType") TestFileType testFileType
                                                  ) {
        Professor professor = (Professor) authentication.getPrincipal();
        log.info("Create new test by the professor with id: " + professor.getId());
        return ResponseEntity.ok(testFacade.updateTestWithFile(id, file, professor.getId(), filename, testFileType));
    }

    @Operation(summary = "Get test files")
    @GetMapping("/{testId}/test-files")
    public ResponseEntity<TestDTO> getCameraImages(
            @NotNull @PathVariable Long testId,
            Authentication authentication
    ) {
        Professor professor = (Professor) authentication.getPrincipal();
        var testFiles = testFacade.getTestFiles(testId);
        return ResponseEntity.ok(testFiles);
    }

}
