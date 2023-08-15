package com.github.bawey.streamdumper;

import com.github.bawey.streamdumper.config.ContextConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class Dumper {
    public static void main(String[] args) {
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", Boolean.TRUE.toString());
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "host");

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(ContextConfig.class);
        context.start();
    }
}
