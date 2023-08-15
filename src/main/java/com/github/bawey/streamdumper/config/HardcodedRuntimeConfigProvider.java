package com.github.bawey.streamdumper.config;

import java.net.URI;
import java.util.Map;

public class HardcodedRuntimeConfigProvider implements RuntimeConfigProvider {
    @Override
    public RuntimeConfig getRuntimeConfig() {
        return RuntimeConfig.builder()
                .playlistFetchingPeriod(3000)
                .playlistLocation(URI.create("https://ddh1.cdndac.lol/ddh1/premium82/tracks-v1a1/mono.m3u8"))
                .headers(Map.of(
                        "Accept", "*/*",
                        "User-agent", "curl/7.81.0",
                        "Origin", "https://superwebplay.xyz",
                        "Referer", "https://superwebplay.xyz/",
                        "Host", "ddh1.cdndac.lol"))
                .httpRequestAttemptsLimit(7)
                .build();
    }
}
