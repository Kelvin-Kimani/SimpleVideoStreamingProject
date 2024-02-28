package com.video.streaming.streamingvideoproject.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {

    private final String minioUrl;
    private final String minioAccessKey;
    private final String minioSecretKey;

    public MinioConfiguration(@Value("${minio.url}") String minioUrl,
                              @Value("${minio.accessKey}") String minioAccessKey,
                              @Value("${minio.secretKey}") String minioSecretKey) {
        this.minioUrl = minioUrl;
        this.minioAccessKey = minioAccessKey;
        this.minioSecretKey = minioSecretKey;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }
}
