package com.unihack.smart_usb.persistance.repository;

import com.unihack.smart_usb.persistance.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExamRepository extends JpaRepository<Exam, Long> {

}
