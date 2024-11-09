package com.unihack.smart_usb.client;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unihack.smart_usb.client.models.UploadSubmissionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class AIClient {
private static final String SUBMIT_FILE_URI = "/upload-and-check";
    private final WebClient webClient;

    public AIClient(WebClient.Builder builder,
                    @Value("${smartusb-ai.http.base-url}") String baseUrl) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }


    public UploadSubmissionResponse uploadFileToAi(MultipartFile submissionFile) {
        log.info("Sending submission file.");
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("test_file", submissionFile.getResource());

        return webClient.post()
                .uri(SUBMIT_FILE_URI)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(UploadSubmissionResponse.class)
                .block();
    }
}
