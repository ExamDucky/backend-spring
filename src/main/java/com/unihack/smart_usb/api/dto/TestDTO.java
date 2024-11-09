package com.unihack.smart_usb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {

    private Long id;
    private String title;
    private int duration;
    private String description;
    private String groupOneTestFileUri;
    private String groupTwoTestFileUri;
    private String blacklistProcessesFileName;
    private Long professorId;
}
