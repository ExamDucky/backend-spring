package com.unihack.smart_usb.client.models;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TestsClientModel {
    private Long examAttemptId;
    private String groupOneTestFileNameUri;
    private String groupTwoTestFileNameUri;
    private String blacklistProcessesFileNameUri;
}
