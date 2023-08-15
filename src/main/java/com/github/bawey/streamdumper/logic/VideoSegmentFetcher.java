package com.github.bawey.streamdumper.logic;

import com.github.bawey.streamdumper.config.RuntimeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class VideoSegmentFetcher {
    private final RuntimeConfig runtimeConfig;
    private final WebClient webClient;

    public VideoSegment fetchSegment(URI segmentUri) {
        log.info("Attempting to fetch a segment from {}", segmentUri);
        return VideoSegment.builder().uri(segmentUri).content(webClient.fetchBytes(segmentUri)).build();
    }
}
