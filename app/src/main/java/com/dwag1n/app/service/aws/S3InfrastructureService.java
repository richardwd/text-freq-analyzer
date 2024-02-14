package com.dwag1n.app.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.dwag1n.app.exceptions.exception.BizException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import static com.dwag1n.app.exceptions.enums.impl.ExceptionEnum.S3_CLIENT_ERROR;

/**
 * @author: Duo Wang
 * @version: v1.0
 */
@Service("s3InfrastructureService")
@Slf4j
public class S3InfrastructureService {
    private final S3AsyncClient s3AsyncClient;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.folderName}")
    private String folderName;
    @Value("${aws.s3.bucket.bucketName}")
    private String bucketName;

    private static final String LOCAL_FILE_PATH = "C:\\epassi\\test.txt";

    public S3InfrastructureService(S3AsyncClient s3AsyncClient,
                                   AmazonS3 amazonS3) {
        this.s3AsyncClient = s3AsyncClient;
        this.amazonS3 = amazonS3;
    }
    public String loadLargeTextFileFromS3Bucket(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        String key = folderName + "/" + fileName + ".txt";
        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .key(key)
                .bucket(bucketName)
                .build();
        CompletableFuture<String> objectCompletableFuture = s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
                .thenApply(response -> {
                    try {
                        return response.asUtf8String();
                    } catch (Exception e) {
                        log.error("Error loading large text file from S3", e);
                        throw new BizException(S3_CLIENT_ERROR);
                    }
                });
        try {
            return objectCompletableFuture.get();
        } catch (Exception e) {
            log.error("Error loading large text file from S3", e);
            throw new BizException(S3_CLIENT_ERROR);
        }
    }

    public CompletableFuture<String> uploadLargeTextFileToS3Bucket(String filePath) {
        Path path = Paths.get(filePath);
        String key = folderName + "/" + path.getFileName().toString();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        CompletableFuture<String> future = s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromFile(path))
                .thenApply(PutObjectResponse::eTag);

        future.whenComplete((resp, err) -> {
            try {
                if (resp != null) {
                    System.out.println("File upload complete");
                } else {
                    // Handle error
                    log.error("Error uploading file to S3", err);
                }
            } finally {
                // Don't forget to close the S3AsyncClient
                s3AsyncClient.close();
            }
        });

        return future;
    }

    public String downloadLargeTextFileFromS3Bucket(String fileName) throws InterruptedException {
        TransferManager tm = TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
        String key = folderName + "/" + fileName + ".txt";
        File targetFile = new File(LOCAL_FILE_PATH);

        Download download = tm.download(bucketName, key, targetFile);
        download.waitForCompletion();
        return LOCAL_FILE_PATH;
    }

    public boolean checkIfFileExists(String fileName) {
        String key = folderName + "/" + fileName + ".txt";
        return amazonS3.doesObjectExist(bucketName, key);
    }
}
