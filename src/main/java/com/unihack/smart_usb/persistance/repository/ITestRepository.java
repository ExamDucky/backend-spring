package com.unihack.smart_usb.persistance.repository;

import com.unihack.smart_usb.persistance.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ITestRepository extends JpaRepository<Test, Long> {

    Optional<Test> findById(Long id);

    Optional<Test> findByTitle(String title);

    Optional<Test> findByTitleAndProfessorId(String title, Long professorId);

    @Query(value = "SELECT t FROM Test t WHERE t.title LIKE ?1% AND t.professor.id=?2", nativeQuery = false)
    List<Test> findByTitleQueryAndProfessorId(String title, Long professorId);

    @Query(value = "SELECT t FROM Test t WHERE t.title LIKE ?1%", nativeQuery = false)
    List<Test> findByTitleQuery(String title);

}
