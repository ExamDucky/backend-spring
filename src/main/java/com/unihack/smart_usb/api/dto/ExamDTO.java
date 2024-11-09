package com.unihack.smart_usb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamDTO {

    Long id;
    Long professorId;
    String classroomName;
    ZonedDateTime examStarted;
    ZonedDateTime examEnded;
    TestDTO testDTO;
    Long testId;

}
