package com.github.bawey.streamdumper.config;

import com.github.bawey.streamdumper.logic.VideoSegmentFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
@EnableScheduling
@ComponentScan(basePackageClasses = VideoSegmentFetcher.class)
@EnableRetry
@Slf4j
public class ContextConfig {

    @Bean
    public RuntimeConfigProvider runtimeConfigProvider() {
        return new HardcodedRuntimeConfigProvider();
    }

    @Bean
    public RuntimeConfig runtimeConfig(RuntimeConfigProvider runtimeConfigProvider) {
        return runtimeConfigProvider.getRuntimeConfig();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

}
