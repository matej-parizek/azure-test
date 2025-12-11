package com.cgi.parizek.matej.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@SpringBootTest(classes = SpringExtension.class)
@EnableConfigurationProperties(PagingProperties.class)
class PagingPropertiesTest {
    @Autowired
    PagingProperties pagingProperties;

    @Test
    void setProperties_success() {
        Assertions.assertEquals(100, pagingProperties.getSize());
        Assertions.assertEquals(2, pagingProperties.getMaxCachePage());
    }
}