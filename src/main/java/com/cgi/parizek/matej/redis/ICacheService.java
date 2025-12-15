package com.cgi.parizek.matej.redis;

public interface ICacheService<T> {

    void save(String key, T value);

    void save(String key, T value, java.time.Duration ttl);

    T get(String key);

    void delete(String key);
}
