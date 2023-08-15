package com.github.bawey.streamdumper.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.util.Map;

/**
 * A wrapper class for all the runtime parameters
 */
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RuntimeConfig {
    private final long playlistFetchingPeriod;
    private Map<String, String> headers;
    private URI playlistLocation;
    private int httpRequestAttemptsLimit;
}
