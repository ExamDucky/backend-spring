package com.unihack.smart_usb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamAttemptDTO {
    private Long id;

    private String studentId;
    private Long examId;
    private int grade;
    private double plagiarismPercent;
    private boolean isValid;
    private String macAddress;

}
