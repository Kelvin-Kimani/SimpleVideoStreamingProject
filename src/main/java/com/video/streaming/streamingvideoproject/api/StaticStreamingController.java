package com.video.streaming.streamingvideoproject.api;

import com.video.streaming.streamingvideoproject.services.StaticStreamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * This class is responsible for handling static streaming requests.
 * It only returns an existing file in the resources path.
 */

@Slf4j
@RestController
@RequestMapping("api/v1/static")
public class StaticStreamingController {

    private final StaticStreamingService staticStreamingService;

    public StaticStreamingController(StaticStreamingService staticStreamingService) {this.staticStreamingService = staticStreamingService;}

    @GetMapping(value = "videos/{title}", produces = "video/mp4")
    public Mono<Resource> streamVideo(@PathVariable String title, @RequestHeader String range) {

        log.info("Request to stream video: {}, range {}", title, range);
        return staticStreamingService.getVideo(title);
    }
}
