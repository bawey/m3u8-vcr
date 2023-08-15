package com.github.bawey.streamdumper.logic;

import com.github.bawey.streamdumper.config.RuntimeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClient {
    private final RuntimeConfig runtimeConfig;

    @Retryable(maxAttemptsExpression = "#{runtimeConfig.getHttpRequestAttemptsLimit()}")
    private <T> T fetch(URI uri, HttpResponse.BodyHandler<T> bodyHandler) {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(uri);
        runtimeConfig.getHeaders().forEach(requestBuilder::setHeader);

        requestBuilder.timeout(Duration.of(10, ChronoUnit.SECONDS));
        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<T> response = httpClient.send(request, bodyHandler);
            log.info("Request to {} returned with {}", uri, response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.warn("Failed to fetch from {} with cause: {}. Will retry if configured to.", uri, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String fetchString(URI uri) {
        return fetch(uri, HttpResponse.BodyHandlers.ofString());
    }

    public byte[] fetchBytes(URI uri) {
        return fetch(uri, HttpResponse.BodyHandlers.ofByteArray());
    }
}
