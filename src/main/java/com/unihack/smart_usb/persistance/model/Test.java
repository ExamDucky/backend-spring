package com.unihack.smart_usb.persistance.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "Test")
@Table(name = "test")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private String groupOneTestFileName;

    @Column(nullable = false)
    private String groupTwoTestFileName;

    @Column(nullable = false)
    private String blacklistProcessesFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @OneToMany(mappedBy = "test", cascade = CascadeType.PERSIST)
    private List<Exam> exams;

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", groupOneTestFileName='" + groupOneTestFileName + '\'' +
                ", groupTwoTestFileName='" + groupTwoTestFileName + '\'' +
                ", blacklistProcessesFileName='" + blacklistProcessesFileName + '\'' +
                ", professor=" + professor +
                ", exams=" + exams +
                '}';
    }
}
