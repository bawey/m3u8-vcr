package com.github.bawey.streamdumper.logic;

import com.github.bawey.streamdumper.config.RuntimeConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SegmentURIsExtractor {
    private final RuntimeConfig runtimeConfig;

    public List<URI> extractFromPlaylist(Playlist playlist) {

        List<URI> uris = new ArrayList<>();

        try {
            URL url = playlist.getUri().toURL();
            for (String line : playlist.getRawBody().split(System.lineSeparator())) {
                if (line.endsWith(".ts")) {
                    String subPath = url.getPath();
                    subPath = subPath.substring(0, subPath.lastIndexOf("/") + 1);
                    uris.add(new URL(url.getProtocol(), url.getHost(), url.getPort(), subPath + line).toURI());
                }
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return uris.stream().collect(Collectors.toList());

    }
}
