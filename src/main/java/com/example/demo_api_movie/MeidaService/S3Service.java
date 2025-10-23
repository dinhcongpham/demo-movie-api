package com.example.demo_api_movie.MeidaService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.bucket.image}")
    private String imageBucket;

    @Value("${aws.bucket.video}")
    private String videoBucket;

    @Value("${AWS_REGION}")
    private String region;

    /**
     * Upload small file directly (backend → S3)
     */
    public String uploadFile(MultipartFile file, boolean isVideo) {
        String bucket = isVideo ? videoBucket : imageBucket;
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return String.format("https://%s.s3.amazonaws.com/%s", bucket, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    /**
     * Generate presigned URL (for large video upload)
     */
    public String generatePresignedUploadUrl(String fileName, String contentType, String type) {
        String bucket = type.equals("video") ? videoBucket : imageBucket;
        String key = UUID.randomUUID() + "-" + fileName;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        return presignedRequest.url().toString();
    }

    public void deleteFile(String fileUrl, String type) {
        if (fileUrl.length() < 2) throw new IllegalArgumentException("Invalid S3 URL");
        String bucket = Objects.equals(type, "video") ? videoBucket : imageBucket;

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileUrl)
                .build());
    }
}
