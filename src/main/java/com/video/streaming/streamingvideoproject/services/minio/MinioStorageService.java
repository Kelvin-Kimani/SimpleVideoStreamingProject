package com.video.streaming.streamingvideoproject.services.minio;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioStorageService {

    private final MinioClient minioClient;
    private final Long putObjectPartSize;

    public MinioStorageService(MinioClient minioClient,
                               @Value("${minio.put-object-part-size}") Long putObjectPartSize) {
        this.minioClient = minioClient;
        this.putObjectPartSize = putObjectPartSize;
    }

    public void save(String uuid, String bucketName, MultipartFile file) throws Exception {

        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .object(uuid)
                        .stream(file.getInputStream(), file.getSize(), putObjectPartSize)
                        .build()
        );
    }

    public InputStream getInputStream(String uuid, String bucketName, long offset, long length) throws Exception {

        return minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(bucketName)
                        .offset(offset)
                        .length(length)
                        .object(uuid)
                        .build()
        );
    }
}
