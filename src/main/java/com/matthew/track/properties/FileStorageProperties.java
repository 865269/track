package com.matthew.track.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * File upload code from https://www.callicoder.com/spring-boot-file-upload-download-rest-api-example/
 */

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
