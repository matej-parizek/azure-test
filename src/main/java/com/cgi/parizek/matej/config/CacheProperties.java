package com.cgi.parizek.matej.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Data
@ConfigurationProperties(prefix = "cache", ignoreInvalidFields = true)
@Component
public class CacheProperties {
    private Duration ttl;
}
