package com.unihack.smart_usb.persistance.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "ExamAttempt")
@Table(name = "exam_attempt")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(nullable = false)
    private int grade;

    private double plagiarismPercent;

    private boolean isValid;

    @Column(name = "submitted_file_name")
    private String submittedFileName;

    @Column(nullable = false)
    private String macAddress;

    @OneToMany(mappedBy = "examAttempt")
    List<ExamAttemptDetail> examAttemptDetails;


    @Override
    public String toString() {
        return "ExamAttempt{" +
                "id=" + id +
                ", student=" + student +
                ", exam=" + exam +
                ", grade=" + grade +
                ", plagiarismPercent=" + plagiarismPercent +
                ", isValid=" + isValid +
                ", macAddress='" + macAddress + '\'' +
                ", screenshots=" + examAttemptDetails +
                '}';
    }
}
