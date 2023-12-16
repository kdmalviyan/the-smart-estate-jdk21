package com.sfd.thesmartestate.cloud.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author kuldeep
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3CloudService {


    private final AmazonS3 amazonS3;
    @Value("${aws.s3.database-backup}")
    private String databaseBackupBucket;

    public void uploadFileToS3(String path,
                               String fileName,
                               MultipartFile file) {

        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        Optional.of(metadata).ifPresent(map -> map.forEach(objectMetadata::addUserMetadata));
        try {
            amazonS3.putObject(path, fileName, file.getInputStream(), objectMetadata);
        } catch (AmazonServiceException | IOException e) {
            log.error("Exception", e);
            throw new FileException("Failed to upload the profile photo", e);
        }
    }

    public void uploadFileStreamToS3(String path, String fileName, InputStream inputStream) {
        //get file metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new FileException("Failed to upload thumbnail the file", e);
        }
    }

    public Resource download(String path, String bucket) {
        try {
            S3Object object = amazonS3.getObject(bucket, path);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return new InputStreamResource(objectContent);
        } catch (AmazonServiceException e) {
            throw new FileException("Failed to download the file", e);
        }
    }

    public void uploadDatabaseBackupFileToS3(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)){
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());
            amazonS3.putObject(databaseBackupBucket, file.getName(), inputStream, metadata);
        } catch (AmazonServiceException | IOException e) {
            throw new FileException("Database backup upload to S3 failed", e);
        }
    }

    public void delete(String path, String bucket) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, path));
    }
}
