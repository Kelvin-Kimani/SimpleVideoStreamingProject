package com.video.streaming.streamingvideoproject.db.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "video_file_metadata")
public class VideoFileMetadataEntity {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @GeneratedValue(generator = "uuid")
    private String id;

    private long size;

    private String httpContentType;
}
