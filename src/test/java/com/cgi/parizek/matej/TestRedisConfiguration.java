package com.cgi.parizek.matej;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@TestConfiguration
public class TestRedisConfiguration {

    private final RedisServer redisServer;

    @SneakyThrows
    public TestRedisConfiguration(RedisProperties redisProperties) {
        this.redisServer = new RedisServer(redisProperties.getPort());
    }

    @SneakyThrows
    @PostConstruct
    public void startRedis() {
        redisServer.start();
    }

    @SneakyThrows
    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
