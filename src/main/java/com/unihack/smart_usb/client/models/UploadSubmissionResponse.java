package com.unihack.smart_usb.client.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadSubmissionResponse {

    String submissionId;
    String status;
    String filePath;
    String timestamp;
}
