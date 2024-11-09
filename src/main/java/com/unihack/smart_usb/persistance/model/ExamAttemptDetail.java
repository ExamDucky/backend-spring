package com.unihack.smart_usb.persistance.model;


import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity(name = "ExamScreenshot")
@Table(name = "exam_screenshot")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExamAttemptDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long id;

    @Column(name = "screenshot_name")
    private String screenshotName;

    @Column(name = "process_list")
    private String processList;

    @Column(nullable = false)
    private ZonedDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_attempt_id")
    private ExamAttempt examAttempt;

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
        return "ExamAttemptDetail{" +
                "id=" + id +
                ", screenshotName='" + screenshotName + '\'' +
                ", processList='" + processList + '\'' +
                ", created=" + created +
                ", examAttempt=" + examAttempt +
                '}';
    }
}
