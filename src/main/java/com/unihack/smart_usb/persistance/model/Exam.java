package com.unihack.smart_usb.persistance.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity(name = "Exam")
@Table(name = "exam")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime created;

    @Column
    private ZonedDateTime ended;
    @Column(nullable = false)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private Test test;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamAttempt> students;

    @PrePersist
    @PreUpdate
    public void setDate() {
        ZonedDateTime now = ZonedDateTime.now();
        if (getCreated() == null) {
            setCreated(now);
        }
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", created=" + created +
                ", location='" + location + '\'' +
                ", test=" + test +
                ", students=" + students +
                '}';
    }
}
