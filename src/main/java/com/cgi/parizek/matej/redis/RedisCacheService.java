package com.cgi.parizek.matej.redis;

import com.cgi.parizek.matej.config.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisCacheService<T> {
    private final CacheProperties properties;

    private final RedisTemplate<String, T> redisTemplate;

    public void save(String key, T value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void save(String key, T value){
        this.save(key,value, properties.getTtl());
    }

    public T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
