package com.unihack.smart_usb.client;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.Response;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlockBlobItem;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.azure.storage.blob.models.TaggedBlobItem;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.azure.storage.common.sas.SasProtocol;
import com.unihack.smart_usb.client.models.TestFileType;
import com.unihack.smart_usb.client.models.TestsClientModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BlobStorageTestsClient {
    private int sasDurationHours;

    private static final String BLOB_STORAGE_ENDPOINT_TEMPLATE = "https://%s.blob.core.windows.net/";

    private final BlobContainerSasPermission blobContainerSasPermission;
    private final BlobContainerClient blobContainerClient;
    private final String blobContainerUrl;

    private final BlobContainerAsyncClient blobContainerAsyncClient;

    public BlobStorageTestsClient(@Value("${smart-usb.storage.account.name}") String storageAccountName,
                                  @Value("${smart-usb.storage.account.key}") String storageAccountKey,
                                  @Value("${smart-usb.storage.container.test.name}") String blobContainerUrl,
                                  @Value("${smart-usb.storage.account.sas.duration.hours}") int sasDurationHours
    ) {
        StorageSharedKeyCredential blobStorageCredential = new StorageSharedKeyCredential(storageAccountName, storageAccountKey);
        this.blobContainerSasPermission = new BlobContainerSasPermission()
                .setReadPermission(true);
        this.blobContainerClient = new BlobContainerClientBuilder()
                .endpoint(String.format(BLOB_STORAGE_ENDPOINT_TEMPLATE, storageAccountName))
                .credential(blobStorageCredential)
                .containerName(blobContainerUrl)
                .buildClient();
        this.blobContainerUrl = blobContainerClient.getBlobContainerUrl();
        this.sasDurationHours = sasDurationHours;
        this.blobContainerAsyncClient = new BlobContainerClientBuilder()
                .endpoint(String.format(BLOB_STORAGE_ENDPOINT_TEMPLATE, storageAccountName))
                .credential(blobStorageCredential)
                .containerName(blobContainerUrl)
                .buildAsyncClient();
    }

    public TestsClientModel getTestFiles(Long testId) {

        BlobServiceSasSignatureValues blobServiceSasSignatureValues = new BlobServiceSasSignatureValues(OffsetDateTime.now().plusHours(this.sasDurationHours), this.blobContainerSasPermission)
                .setProtocol(SasProtocol.HTTPS_ONLY);

        PagedIterable<TaggedBlobItem> taggedBlobItemsGroupOne = this.blobContainerClient.findBlobsByTags(createTagQuery(testId, TestFileType.GroupOne.name()));
        PagedIterable<TaggedBlobItem> taggedBlobItemsGroupTwo = this.blobContainerClient.findBlobsByTags(createTagQuery(testId, TestFileType.GroupTwo.name()));
        PagedIterable<TaggedBlobItem> taggedBlobItemsGroupProcessBlacklist = this.blobContainerClient.findBlobsByTags(createTagQuery(testId, TestFileType.ProcessBlacklist.name()));

        List<TaggedBlobItem> taggedBlobItems = new ArrayList<>();

        for (var item : taggedBlobItemsGroupOne) {
            taggedBlobItems.add(item);
        }
        for (var item : taggedBlobItemsGroupTwo) {
            taggedBlobItems.add(item);
        }
        for (var item : taggedBlobItemsGroupProcessBlacklist) {
            taggedBlobItems.add(item);
        }

        var newTestClient = TestsClientModel.builder().build();

        for (var taggedBlobItem : taggedBlobItems) {
            var testFileType = TestFileType.valueOf(taggedBlobItem.getTags().get("testFileType"));
            var fileUri = createImageUri(this.blobContainerUrl, taggedBlobItem.getName(), blobContainerClient.generateSas(blobServiceSasSignatureValues));
            switch (testFileType) {
                case GroupOne -> {
                    newTestClient.setGroupOneTestFileNameUri(fileUri);
                }
                case GroupTwo -> {
                    newTestClient.setGroupTwoTestFileNameUri(fileUri);
                }
                case ProcessBlacklist -> {
                    newTestClient.setBlacklistProcessesFileNameUri(fileUri);
                }
            }
        }

        return newTestClient;
    }

    public void uploadTestFile(Long testId, String filename, MultipartFile testFile, TestFileType testFileType) throws IOException {
        BlobAsyncClient blobAsyncClient = this.blobContainerAsyncClient.getBlobAsyncClient(filename);

        Map<String, String> tags = new HashMap<String, String>();
        tags.put("testId", testId.toString());
        tags.put("testFileType", testFileType.name());
        tags.put("created", ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        tags.put("updated", ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        long blockSize = 512L * 1024L; //512KB
        ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions()
                .setBlockSizeLong(blockSize)
                .setMaxConcurrency(5);
        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentType("application/zip");


        Flux<ByteBuffer> fileFlux = Flux.just(ByteBuffer.wrap(testFile.getBytes()));

        BlobParallelUploadOptions options = new BlobParallelUploadOptions(fileFlux)
                .setTags(tags)
                .setParallelTransferOptions(parallelTransferOptions)
                .setHeaders(headers);

        Mono<Response<BlockBlobItem>> responseMono = blobAsyncClient.uploadWithResponse(options);
        Response<BlockBlobItem> response = responseMono.block();
        log.info("Successfully test .zip file " + filename + " with tags " + tags);
    }

    public void createExamSubmission(Long studentId, Long examAttemptId, MultipartFile testFile, String filename, TestFileType testFileType) throws IOException {
        BlobAsyncClient blobAsyncClient = this.blobContainerAsyncClient.getBlobAsyncClient(filename);

        Map<String, String> tags = new HashMap<String, String>();
        tags.put("examAttemptId", examAttemptId.toString());
        tags.put("studentId", studentId.toString());
        tags.put("testFileType", testFileType.name());
        tags.put("created", ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
        tags.put("updated", ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));

        long blockSize = 512L * 1024L; //512KB
        ParallelTransferOptions parallelTransferOptions = new ParallelTransferOptions()
                .setBlockSizeLong(blockSize)
                .setMaxConcurrency(5);
        BlobHttpHeaders headers = new BlobHttpHeaders();
        headers.setContentType("application/zip");


        Flux<ByteBuffer> fileFlux = Flux.just(ByteBuffer.wrap(testFile.getBytes()));

        BlobParallelUploadOptions options = new BlobParallelUploadOptions(fileFlux)
                .setTags(tags)
                .setParallelTransferOptions(parallelTransferOptions)
                .setHeaders(headers);

        Mono<Response<BlockBlobItem>> responseMono = blobAsyncClient.uploadWithResponse(options);
        Response<BlockBlobItem> response = responseMono.block();
        log.info("Successfully test .zip file " + filename + " with tags " + tags);
    }

    private String createImageUri(String containerUrl, String imageName, String sasToken) {
        return containerUrl + "/" + imageName + "?" + sasToken;
    }

    private String createTagQuery(Long testId, String testFileType) {
        return "\"testId\"='" + testId + "' AND \"testFileType\"='" + testFileType + "'";
    }

}
