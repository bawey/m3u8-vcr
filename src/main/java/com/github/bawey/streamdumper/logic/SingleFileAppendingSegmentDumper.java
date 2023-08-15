package com.github.bawey.streamdumper.logic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

@Slf4j
@Component
public class SingleFileAppendingSegmentDumper implements SegmentDumper {
    @Override
    public void dumpSegment(VideoSegment segment) {
        File output = Paths.get(System.getProperty("user.home"), "dumped-stream.ts").toFile();

        try (FileOutputStream fos = new FileOutputStream(output, true);) {
            fos.write(segment.getContent());
        } catch (Exception e) {
            log.error("Failed to append the file: " + output.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }

    }
}
