package com.video.streaming.streamingvideoproject.data;

public record VideoWithMetaData(
        String id,
        Long size,
        String httpContentType,
        byte[] data
) {
}
