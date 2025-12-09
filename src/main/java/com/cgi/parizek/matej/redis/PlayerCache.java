package com.cgi.parizek.matej.redis;


import com.cgi.parizek.matej.config.CacheProperties;
import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.entity.Mission;
import com.cgi.parizek.matej.entity.Player;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class PlayerCache extends RedisCache<Player> implements IPlayerCache {
    /**
     * page:{page}:{size}:mission
     */
    private static final String MISSION_KEY = "page:%s:%s:mission";
    private final PagingProperties pagingProperties;

    public PlayerCache(CacheProperties properties, RedisTemplate<String, Object> redisTemplate, PagingProperties pagingProperties) {
        super(properties, redisTemplate, Player.class);
        this.pagingProperties = pagingProperties;
    }

    @Override
    public void saveMissions(String key, List<Mission> value, Integer page) {
        redisTemplate.opsForHash().put(key, MISSION_KEY.formatted(page, pagingProperties.getSize()), value);
    }

    @Override
    public void save(String key, Player value) {
        super.save(key, value);
    }

    @Override
    public void save(String key, Player value, Duration ttl) {
        super.save(key, value, ttl);
    }

    @Override
    public List<Mission> getMissions(String key, Integer page) {

        return (List<Mission>) redisTemplate.opsForHash()
                .get(key, MISSION_KEY.formatted(page, pagingProperties.getSize()));
    }
}
