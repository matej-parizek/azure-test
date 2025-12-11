package com.cgi.parizek.matej.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Data
@ConfigurationProperties(prefix = "spring.data.redis", ignoreInvalidFields = true)
@Component
public class CacheProperties {
    private Duration ttl;
    @Value("${ssl.enabled:false}") private boolean ssl;
    private String host;
    private String password;
    private Integer port;
}
