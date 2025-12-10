package com.cgi.parizek.matej.redis;


import com.cgi.parizek.matej.config.CacheProperties;
import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.PlayerDto;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerCache extends RedisCache<PlayerDto> implements IPlayerCache {
    /**
     * page:{page}:{size}:mission
     */
    private static final String MISSION_KEY = "page:%s:%s:mission";
    private final PagingProperties pagingProperties;

    public PlayerCache(CacheProperties properties,
                       RedisTemplate<String, Object> redisTemplate,
                       PagingProperties pagingProperties,
                       ObjectMapper objectMapper
    ) {
        super(properties, redisTemplate, objectMapper.getTypeFactory()
                .constructType(PlayerDto.class), objectMapper);
        this.pagingProperties = pagingProperties;
    }

    @Override
    public void saveMissions(String key, List<MissionDto> value, Integer page) {
        redisTemplate.opsForHash().put(key, MISSION_KEY.formatted(page, pagingProperties.getSize()), value);
    }

    @Override
    public List<MissionDto> getMissions(String key, Integer page) {
        Object raw = redisTemplate.opsForHash()
                .get(key, MISSION_KEY.formatted(page, pagingProperties.getSize()));
        if (raw == null)
            return null;
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, MissionDto.class);

        try {
            return objectMapper.convertValue(raw, type);
        } catch (IllegalArgumentException ex) {
            redisTemplate.opsForHash().delete(key, MISSION_KEY.formatted(page, pagingProperties.getSize()));
            return null;
        }
    }

}
