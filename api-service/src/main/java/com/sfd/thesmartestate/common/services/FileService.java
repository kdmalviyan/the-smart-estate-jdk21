package com.sfd.thesmartestate.common.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface FileService {

    void uploadFileToS3(String path,
                        String fileName,
                        MultipartFile inputStream);
    void uploadFileStreamToS3(String path,
                        String fileName,
                              InputStream inputStream);

    Resource download(String path, String bucket);

    void delete(String path, String bucket);

    void uploadDatabaseBackupFileToS3(File file);
}
