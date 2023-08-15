package com.github.bawey.streamdumper.logic;

import com.github.bawey.streamdumper.config.RuntimeConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class PlaylistFetcher {
    private final RuntimeConfig runtimeConfig;
    private final WebClient webClient;

    public Playlist fetchPlaylist() {
        final URI sourceUri = runtimeConfig.getPlaylistLocation();
        String playlistBody = webClient.fetchString(sourceUri);
        return Playlist.builder().uri(sourceUri).rawBody(playlistBody).build();
    }
}
