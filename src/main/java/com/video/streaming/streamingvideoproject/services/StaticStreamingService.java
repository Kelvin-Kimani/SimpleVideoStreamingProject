package com.video.streaming.streamingvideoproject.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StaticStreamingService {

    private final ResourceLoader resourceLoader;

    public StaticStreamingService(ResourceLoader resourceLoader) {this.resourceLoader = resourceLoader;}

    public Mono<Resource> getVideo(String title) {
        return Mono.fromSupplier(() -> resourceLoader.getResource("classpath:videos/" + title + ".mp4"));
    }
}
