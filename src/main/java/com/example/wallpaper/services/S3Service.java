package com.example.wallpaper.services;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
public class S3Service {


    private final S3Client s3Client;

    @Value("${aws.bucket-name}")
    private String bucketName;

    @Value("${aws.access-key}") String accessID;
    @Value("${aws.secret-access-key}") String secretAccessID;
    @Value("${aws.bucket-region}") String region;

    public S3Service (
        @Value("${aws.access-key}") String accessID,
        @Value("${aws.secret-access-key}") String secretAccessID,
        @Value("${aws.bucket-region}") String region) {
            this.s3Client = S3Client.builder()
                                    .region(Region.AP_SOUTH_1)
                                    .credentialsProvider(
                                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessID, secretAccessID))
                                    )
                                    .build();
    }

    public String uploadFile(MultipartFile file) {
        String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                                .bucket(bucketName)
                                .key(uniqueFileName)
                                .contentType(file.getContentType())
                                .build(),
                            RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
            return uniqueFileName;
        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Failed to upload",e);
        }
    }

    public String generatePresignedUrl(String imageName) {
        try (S3Presigner presigner = S3Presigner.builder().region(Region.AP_SOUTH_1).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessID, secretAccessID))).build()) {

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofHours(2))
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();
        }
    }
    
}
