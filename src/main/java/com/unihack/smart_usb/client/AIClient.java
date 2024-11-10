package com.unihack.smart_usb.client;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unihack.smart_usb.client.models.UploadSubmissionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@Slf4j
public class AIClient {
    private static final String SUBMIT_FILE_URI = "/upload-and-check/";
    private final WebClient webClient;

    public AIClient(WebClient.Builder builder,
                    @Value("${smartusb-ai.http.base-url}") String baseUrl) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(false))) // Disable automatic redirect following
                .build();
    }


    public UploadSubmissionResponse uploadFileToAi(MultipartFile submissionFile) {
        log.info("Sending submission file.");
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("test_file", submissionFile.getResource());

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(SUBMIT_FILE_URI)
                        .queryParam("name", "program.c") // Adding the query parameter
                        .build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(formData)
                .exchangeToMono(response -> {
                    // Print the status code
                    System.out.println("Status code: " + response.statusCode());

                    // Check if the response is a redirect (307 status)
                    if (response.statusCode() == HttpStatus.TEMPORARY_REDIRECT) {
                        // Extract the redirect URL from the "Location" header
                        String redirectUrl = response.headers().header(HttpHeaders.LOCATION).get(0);
                        System.out.println("Redirecting to: " + redirectUrl);
                    }

                    // Check if the response is successful (2xx) or an error
                    if (response.statusCode().is2xxSuccessful()) {
                        // Return the response body as FileUploadResponse if successful
                        return response.bodyToMono(UploadSubmissionResponse.class)
                                .doOnNext(body -> {
                                    // Print the response body
                                    System.out.println("Response Body: " + body);
                                });
                    } else {
                        // If there's an error, return the error body and log it
                        return response.bodyToMono(String.class)
                                .doOnNext(errorBody -> {
                                    // Print the error response body
                                    System.err.println("Error Response: " + errorBody);
                                })
                                .flatMap(errorBody -> Mono.error(new RuntimeException("Server returned an error: " + errorBody)));
                    }
                })
                .doOnError(Throwable::printStackTrace) // Print any error stack trace
                .block();
    }
}
