package com.github.bawey.streamdumper.logic;

import com.github.bawey.streamdumper.config.RuntimeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class MainLoop implements Runnable {
    private final RuntimeConfig runtimeConfig;
    private final PlaylistFetcher playlistFetcher;
    private final VideoSegmentFetcher videoSegmentFetcher;
    private final SegmentURIsExtractor segmentURIsExtractor;
    private final List<SegmentDumper> segmentDumpers;
    private final Set<URI> handledURIs = new HashSet<>();

    @Override
    @Scheduled(fixedRateString = "#{runtimeConfig.getPlaylistFetchingPeriod()}")
    public void run() {
        log.debug("Launching the main loop");
        Playlist playlist = playlistFetcher.fetchPlaylist();
        log.debug("Fetched some playlist: \n {}", playlist.getRawBody());
        List<URI> segmentUris = segmentURIsExtractor.extractFromPlaylist(playlist);
        log.info("Extracted {} segment URIs from the playlist", segmentUris.size());
        segmentUris.stream().filter(Predicate.not(handledURIs::contains)).map(videoSegmentFetcher::fetchSegment).forEach(segment -> segmentDumpers.forEach(dumper -> dumper.dumpSegment(segment)));
        handledURIs.addAll(segmentUris);
    }
}
