package com.video.streaming.streamingvideoproject.db.repositories;

import com.video.streaming.streamingvideoproject.db.models.VideoFileMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoFileMetadataRepository extends JpaRepository<VideoFileMetadataEntity, String> {

}
