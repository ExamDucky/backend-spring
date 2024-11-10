package com.unihack.smart_usb.client.models;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadSubmissionResponse {
    @JsonProperty("submission_id")
    String submissionId;
    String status;
    @JsonProperty("file_path")
    String filePath;
    String timestamp;
}
