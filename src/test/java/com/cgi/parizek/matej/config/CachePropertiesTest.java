package com.cgi.parizek.matej.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;


@ActiveProfiles("test")
@SpringBootTest(classes = CacheProperties.class)
@EnableConfigurationProperties(CacheProperties.class)
class CachePropertiesTest {
    @Autowired
    CacheProperties cacheProperties;

    @Test
    @DisplayName("Test 'spring.data.redis' property loading")
    void readProperties_success() {
        Assertions.assertEquals(Duration.ofMinutes(10), cacheProperties.getTtl());
        Assertions.assertEquals("", cacheProperties.getPassword());
        Assertions.assertEquals(6379, cacheProperties.getPort());
        Assertions.assertEquals("localhost", cacheProperties.getHost());
    }
}