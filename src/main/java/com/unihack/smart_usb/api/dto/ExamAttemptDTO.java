package com.unihack.smart_usb.api.dto;

import com.unihack.smart_usb.client.models.PlagiarismLevel;
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
    private PlagiarismLevel plagiarismLevel;
    private boolean isValid;
    private String macAddress;

}
