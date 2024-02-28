package com.video.streaming.streamingvideoproject.services;

import com.video.streaming.streamingvideoproject.data.Range;
import com.video.streaming.streamingvideoproject.data.VideoWithMetaData;
import com.video.streaming.streamingvideoproject.db.models.VideoFileMetadataEntity;
import com.video.streaming.streamingvideoproject.db.repositories.VideoFileMetadataRepository;
import com.video.streaming.streamingvideoproject.platform.exceptions.NotFoundException;
import com.video.streaming.streamingvideoproject.platform.exceptions.StorageException;
import com.video.streaming.streamingvideoproject.services.minio.MinioStorageService;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class DynamicStreamingService {

    private static final String BUCKET_NAME = "videos";
    private final MinioStorageService storageService;
    private final VideoFileMetadataRepository fileMetadataRepository;

    public DynamicStreamingService(MinioStorageService storageService,
                                   VideoFileMetadataRepository fileMetadataRepository) {
        this.storageService = storageService;
        this.fileMetadataRepository = fileMetadataRepository;
    }

    public String save(MultipartFile video) {

        try {

            var metadata = new VideoFileMetadataEntity();

            metadata.setSize(video.getSize());
            metadata.setHttpContentType(video.getContentType());

            var saved = fileMetadataRepository.save(metadata);

            storageService.save(saved.getId(), BUCKET_NAME, video);

            return saved.getId();
        } catch (Exception ex) {

            log.error("Exception occurred when trying to save the file", ex);
            throw new StorageException(ex);
        }
    }

    public VideoWithMetaData fetchChunk(String id, Range range) {

        var fileMetadata = fileMetadataRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Video file with id %snot found"));

        return new VideoWithMetaData(
                fileMetadata.getId(),
                fileMetadata.getSize(),
                fileMetadata.getHttpContentType(),
                readChunk(id, range, fileMetadata.getSize())
        );
    }

    private byte[] readChunk(String uuid, Range range, long fileSize) {
        var startPosition = range.getRangeStart();
        var endPosition = range.getRangeEnd(fileSize);
        int chunkSize = (int) (endPosition - startPosition + 1);
        try (InputStream inputStream = storageService.getInputStream(uuid, BUCKET_NAME, startPosition, chunkSize)) {
            return inputStream.readAllBytes();
        } catch (Exception exception) {
            log.error("Exception occurred when trying to read file with ID = {}", uuid);
            throw new StorageException(exception);
        }
    }
}

