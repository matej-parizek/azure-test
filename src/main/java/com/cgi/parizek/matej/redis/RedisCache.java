package com.cgi.parizek.matej.redis;

import com.cgi.parizek.matej.config.CacheProperties;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
public abstract class RedisCache<T> implements ICacheService<T> {
    protected final CacheProperties properties;
    protected final RedisTemplate<String, Object> redisTemplate;
    protected final JavaType type;
    protected final ObjectMapper objectMapper;


    @Override
    public void save(String key, T value, Duration ttl) {
        redisTemplate.opsForHash().put(key, "last_sync_at", Instant.now().toEpochMilli());
        redisTemplate.opsForHash().put(key, "data", value);
        redisTemplate.opsForHash().expiration(key, ttl);
        log.debug("Saving object {} key {} with ttl {}", value, key, ttl);
    }

    @Override
    public void save(String key, T value) {
        this.save(key, value, properties.getTtl());
    }

    @Override
    public T get(String key) {
        var raw = redisTemplate.opsForHash().get(key, "data");
        try {
            return objectMapper.convertValue(raw, type);
        } catch (IllegalArgumentException e) {
            log.error("Cannot convert data", e);
            delete(key);
            return null;
        }
    }


    @Override
    public void delete(String key) {
        log.debug("Deleting cache key {}", key);
        redisTemplate.delete(key);
    }
}
