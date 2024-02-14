package com.dwag1n.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TextFreqAnalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextFreqAnalyzerApplication.class, args);
    }

}
