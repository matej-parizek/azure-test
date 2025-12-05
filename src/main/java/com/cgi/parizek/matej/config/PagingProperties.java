package com.cgi.parizek.matej.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("paging")
public class PagingProperties {
    private int size;
}
