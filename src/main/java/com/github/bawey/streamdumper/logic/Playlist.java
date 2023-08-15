package com.github.bawey.streamdumper.logic;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URI;

@RequiredArgsConstructor
@Builder
@Getter
public class Playlist {
    private final URI uri;
    private final String rawBody;
}
