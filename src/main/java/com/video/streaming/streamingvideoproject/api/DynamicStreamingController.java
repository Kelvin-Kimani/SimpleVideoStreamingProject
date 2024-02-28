package com.video.streaming.streamingvideoproject.api;

import com.video.streaming.streamingvideoproject.data.Range;
import com.video.streaming.streamingvideoproject.services.DynamicStreamingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is responsible for handling dynamic streaming requests.
 * It saves object as well as metadata of the object that is being uploaded and subsequently downloaded
 */

@Slf4j
@RestController
@RequestMapping("api/v1/dynamic")
public class DynamicStreamingController {

    private static final int DEFAULT_CHUNK_SIZE = 3145728;
    private final DynamicStreamingService dynamicStreamingService;

    public DynamicStreamingController(DynamicStreamingService dynamicStreamingService) {this.dynamicStreamingService = dynamicStreamingService;}

    @PostMapping(path = "videos")
    public ResponseEntity<String> save(@RequestParam("file") MultipartFile file) {
        var fileUuid = dynamicStreamingService.save(file);
        return ResponseEntity.ok(fileUuid);
    }

    @GetMapping(path = "videos/{id}")
    public ResponseEntity<byte[]> readChunk(
            @RequestHeader(value = HttpHeaders.RANGE, required = false) String range,
            @PathVariable String id
    ) {

        Range parsedRange = Range.parseHttpRangeString(range, DEFAULT_CHUNK_SIZE);

        var videoWithMetaData = dynamicStreamingService.fetchChunk(id, parsedRange);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, videoWithMetaData.httpContentType())
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, calculateContentLengthHeader(parsedRange, videoWithMetaData.size()))
                .header(HttpHeaders.CONTENT_RANGE, constructContentRangeHeader(parsedRange, videoWithMetaData.size()))
                .body(videoWithMetaData.data());
    }

    private String calculateContentLengthHeader(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    private String constructContentRangeHeader(Range range, long fileSize) {
        return "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }
}
