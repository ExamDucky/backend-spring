package com.unihack.smart_usb.facade;

import com.unihack.smart_usb.api.dto.TestDTO;
import com.unihack.smart_usb.client.BlobStorageTestsClient;
import com.unihack.smart_usb.client.models.TestFileType;
import com.unihack.smart_usb.exception.auth.EntityDoesNotExistException;
import com.unihack.smart_usb.exception.auth.UserDoesNotOwnEntityException;
import com.unihack.smart_usb.persistance.model.Professor;
import com.unihack.smart_usb.persistance.model.Test;
import com.unihack.smart_usb.service.implementations.ProfessorService;
import com.unihack.smart_usb.service.implementations.TestService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestFacade {

    private final TestService testService;
    private final ProfessorService professorService;
    @Resource
    private BlobStorageTestsClient testBlobStorageClient;


    public TestDTO getTestFiles(Long testId){
        var testFiles = testBlobStorageClient.getTestFiles(testId);
        return TestDTO.builder()
                .groupOneTestFileUri(testFiles.getGroupOneTestFileNameUri())
                .groupTwoTestFileUri(testFiles.getGroupTwoTestFileNameUri())
                .blacklistProcessesFileName(testFiles.getBlacklistProcessesFileNameUri())
                .build();
    }

    @Transactional
    public TestDTO getTestById(Long id, Professor professor) {
        Optional<Test> dbTestOptional = testService.getTestById(id);
        Test dbTest;
        if (dbTestOptional.isPresent()) {
            dbTest = dbTestOptional.get();
            if (dbTest.getProfessor() != null) {
                if (dbTest.getProfessor().getId() == professor.getId()) {
                    return TestDTO.builder()
                            .id(dbTest.getId())
                            .duration(dbTest.getDuration())
                            .groupOneTestFileUri(dbTest.getGroupOneTestFileName())
                            .groupTwoTestFileUri(dbTest.getGroupTwoTestFileName())
                            .blacklistProcessesFileName(dbTest.getBlacklistProcessesFileName())
                            .professorId(dbTest.getProfessor().getId())
                            .title(dbTest.getTitle())
                            .build();
                } else {
                    throw new UserDoesNotOwnEntityException("The professor does not own the selected test.");
                }
            } else {
                throw new UserDoesNotOwnEntityException("The professor does not own the selected test.");
            }
        } else {
            throw new EntityDoesNotExistException("A test with the given id does not exist.");
        }
    }

    //TODO mozda treba, al verovatno ne, treba by title i prof Id
    public TestDTO getTestByTitle(String title, Professor professor) {
        Optional<Test> dbTestOptional = testService.getTestByTitle(title);
        Test dbTest;
        if (dbTestOptional.isPresent()) {
            dbTest = dbTestOptional.get();
            if (dbTest.getProfessor() != null) {
                if (dbTest.getProfessor().getId() == professor.getId()) {
                    return TestDTO.builder()
                            .id(dbTest.getId())
                            .duration(dbTest.getDuration())
                            .groupOneTestFileUri(dbTest.getGroupOneTestFileName())
                            .groupTwoTestFileUri(dbTest.getGroupTwoTestFileName())
                            .blacklistProcessesFileName(dbTest.getBlacklistProcessesFileName())
                            .professorId(dbTest.getProfessor().getId())
                            .title(dbTest.getTitle())
                            .build();
                } else {
                    throw new UserDoesNotOwnEntityException("The professor does not own the selected test.");
                }
            } else {
                throw new UserDoesNotOwnEntityException("The professor does not own the selected test.");
            }
        } else {
            throw new EntityDoesNotExistException("A test with the given title does not exist.");
        }
    }

    public List<TestDTO> getTestByTitleQuery(String titleQuery, Professor professor) {
        List<Test> tests = testService.getTestByTitleQueryAndProfessorId(titleQuery, professor.getId());
        return tests.stream().map(dbTest -> TestDTO.builder()
                .id(dbTest.getId())
                .duration(dbTest.getDuration())
                .groupOneTestFileUri(dbTest.getGroupOneTestFileName())
                .groupTwoTestFileUri(dbTest.getGroupTwoTestFileName())
                .blacklistProcessesFileName(dbTest.getBlacklistProcessesFileName())
                .professorId(dbTest.getProfessor().getId())
                .title(dbTest.getTitle())
                .build()).toList();
    }

    public TestDTO createNewTest(TestDTO testDto, Long professorId) {
        Optional<Professor> professorOptional = professorService.getProfessorById(professorId);
        Professor professor;
        if (!professorOptional.isPresent()) {
            throw new EntityDoesNotExistException("Professor with the given id does not exist.");
        }
        professor = professorOptional.get();
        Test testToSave = Test.builder()
                .title(testDto.getTitle())
                .description(testDto.getDescription())
                .professor(professor)
                .groupOneTestFileName(testDto.getGroupOneTestFileUri())
                .groupTwoTestFileName(testDto.getGroupTwoTestFileUri())
                .blacklistProcessesFileName(testDto.getBlacklistProcessesFileName())
                .duration(testDto.getDuration())
                .build();

        Test savedTest = testService.saveTest(testToSave);

        return TestDTO.builder()
                .id(savedTest.getId())
                .title(savedTest.getTitle())
                .duration(savedTest.getDuration())
                .description(savedTest.getDescription())
                .groupOneTestFileUri(savedTest.getGroupOneTestFileName())
                .groupTwoTestFileUri(savedTest.getGroupTwoTestFileName())
                .blacklistProcessesFileName(savedTest.getBlacklistProcessesFileName())
                .professorId(professor.getId())
                .build();
    }

    public boolean updateTestWithFile(Long id, MultipartFile file, Long professorId, String filename, TestFileType testFileType) {
        Optional<Test> testOptional = testService.getTestById(id);
        if (!testOptional.isPresent()) {
            throw new EntityDoesNotExistException("The test with the given id does not exist.");
        }
        Test test = testOptional.get();
        Optional<Professor> professorOptional = professorService.getProfessorById(test.getProfessor().getId());
        if (!professorOptional.isPresent()) {
            throw new EntityDoesNotExistException("The professor with the given id does not exist");
        }
        Professor professor = professorOptional.get();

        if (professor.getId() != professorId) {
            throw new UserDoesNotOwnEntityException("The given test does not belong to this professor.");
        }

        //TODO poslati fajlove na S3; videti kako fajlovi treba da se zovu; treba da budu u folderu profesora

        if (file.isEmpty()) {
            throw new EntityDoesNotExistException("File is empty.");
        }

        try{
            testBlobStorageClient.uploadTestFile(id, filename + id.toString(), file, testFileType);
        } catch (Exception e){
            return false;
        }

        return true;

        // Ovo je visak ja bih rekao jer mi upload-ujemo fajl po fajl
        // Neka vraca neki boolean samo ako je sve uspesno ne mora da salje objekat frontu

//        List<String> fileContents = new ArrayList<>();
//        int fileCount = 0;
//
//        try (InputStream inputStream = file.getInputStream();
//             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
//
//            ZipEntry entry;
//            while ((entry = zipInputStream.getNextEntry()) != null) {
//                fileCount++;
//
//                if (!entry.isDirectory()) {
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[1024];
//                    int len;
//                    while ((len = zipInputStream.read(buffer)) > 0) {
//                        byteArrayOutputStream.write(buffer, 0, len);
//                    }
//                    fileContents.add("File Name: " + entry.getName() + "\nContent:\n"
//                            + new String(byteArrayOutputStream.toByteArray()));
//                }
//                zipInputStream.closeEntry();
//            }
//            fileContents.forEach(System.out::println);
//
//            System.out.println("All 3 files processed successfully.\nContents:\n" + String.join("\n\n", fileContents));
//
//
//            return TestDTO.builder()
//                    .id(test.getId())
//                    .duration(test.getDuration())
//                    .title(test.getTitle())
//                    .groupOneTestFileName(test.getGroupOneTestFileName())
//                    .groupTwoTestFileName(test.getGroupTwoTestFileName())
//                    .blacklistProcessesFileName(test.getBlacklistProcessesFileName())
//                    .description(test.getDescription())
//                    .professorId(professor.getId())
//                    .build();
//
//        } catch (IOException e) {
//            throw new EntityDoesNotExistException("Could not process zip file.");
//        }
    }
}
