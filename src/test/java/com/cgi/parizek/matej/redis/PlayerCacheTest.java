package com.cgi.parizek.matej.redis;

import com.cgi.parizek.matej.EntityFactory;
import com.cgi.parizek.matej.config.CacheProperties;
import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.config.RedisConfig;
import com.cgi.parizek.matej.dto.MissionDto;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Import(RedisConfig.class)
class PlayerCacheTest {

    @Mock
    private CacheProperties cacheProperties;

    @Mock
    private PagingProperties pagingProperties;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    private PlayerCache playerCache;

    private static final String KEY = "page:1:100:mission";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(pagingProperties.getSize()).thenReturn(5);

        playerCache = new PlayerCache(cacheProperties, redisTemplate, pagingProperties, objectMapper);
    }

    @Test
    @DisplayName("Save missions into redis hash with formatted field")
    void save_missions_puts_into_hash_with_formatted_field() {
        List<MissionDto> missions = List.of(new MissionDto(), new MissionDto());
        int page = 2;

        playerCache.saveMissions(KEY, missions, page);

        String expectedField = String.format("page:%s:%s:mission", page, pagingProperties.getSize());
        verify(hashOperations, times(1)).put(eq(KEY), eq(expectedField), eq(missions));
    }

    @Test
    @DisplayName("Retrieve mission when raw data is null")
    void retrieve_mission_raw_null() {
        int page = 1;
        when(hashOperations.get(eq(KEY), any())).thenReturn(null);

        List<MissionDto> result = playerCache.getMissions(KEY, page);

        assertNull(result);
        verify(hashOperations, never()).delete(anyString(), any());
    }


    @Test
    @DisplayName("Retrieve mission when conversion succeeds returns list of MissionDto")
    void retrieve_mission_conversion_success() {
        int page = 3;
        List<MissionDto> raw = List.of(EntityFactory.missionDto(1L).build(),
                EntityFactory.missionDto(2L).build());

        objectMapper.registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(pagingProperties.getSize()).thenReturn(5);
        when(hashOperations.get("player:1:mission", "page:3:5:mission"))
                .thenReturn(raw);

        List<MissionDto> result = playerCache.getMissions("player:1:mission", page);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof MissionDto);
        assertTrue(result.get(1) instanceof MissionDto);

        verify(hashOperations, never()).delete(anyString(), any());
    }

    @Test
    @DisplayName("Retrieve mission when conversion fails deletes field and returns null")
    void retrieve_mission_conversion_fails_deletes_and_returns_null() {
        int page = 4;
        Object raw = new Object();
        when(hashOperations.get(eq(KEY), any())).thenReturn(raw);

        doThrow(new IllegalArgumentException("invalid format"))
                .when(objectMapper).convertValue(eq(raw), any(JavaType.class));

        List<MissionDto> result = playerCache.getMissions(KEY, page);

        assertNull(result);

        String expectedField = String.format("page:%s:%s:mission", page, pagingProperties.getSize());
        verify(hashOperations, times(1)).delete(KEY, expectedField);
    }
}
