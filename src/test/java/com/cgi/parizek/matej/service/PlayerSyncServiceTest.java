package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.EntityFactory;
import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.redis.IPlayerCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.temporal.ChronoUnit;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerSyncServiceTest {

    @Mock
    private IMissionService missionService;

    @Mock
    private IPlayerService playerService;

    @Mock
    private IPlayerCache redisCacheService;

    @Mock
    private PagingProperties pagingProperties;

    @InjectMocks
    private PlayerSyncService service;

    @Test
    @DisplayName("Success -> Testing loading  non-cached Player")
    void load_success_nonCached() {
        var playerId = 1L;
        var player = EntityFactory.player(playerId);
        var pageNumber = 2;
        var key = "player:%s:missions".formatted(playerId);

        when(playerService.retrieve(eq(playerId))).thenReturn(player);
        when(missionService.retrieveByPlayerId(eq(playerId), any(Pageable.class))).thenReturn(Page.empty());
        when(pagingProperties.getSize()).thenReturn(20);
        when(pagingProperties.getMaxCachePage()).thenReturn(2);
        when(redisCacheService.get(eq(key))).thenReturn(null);
        when(redisCacheService.getMissions(eq(key), eq(pageNumber))).thenReturn(null);

        var result = service.load(playerId, pageNumber);

        Assertions.assertEquals(result.missions(), emptyList());
        Assertions.assertEquals(result.status(), player.getStatus());
        Assertions.assertEquals(result.username(), player.getUsername());
        verify(missionService, times(1))
                .retrieveByPlayerId(anyLong(), any(Pageable.class));
        verify(playerService, times(1)).retrieve(anyLong());
        verify(redisCacheService, times(1)).saveMissions(anyString(), anyList(), anyInt());
        verify(redisCacheService, times(1)).save(anyString(), any());
    }

    @Test
    @DisplayName("Success -> Testing loading  non-cached Player, with grater page number than enabled to cache")
    void load_success_nonCached_greater_page() {
        var playerId = 1L;
        var player = EntityFactory.player(playerId);
        var pageNumber = 3;
        var key = "player:%s:missions".formatted(playerId);

        when(playerService.retrieve(eq(playerId))).thenReturn(player);
        when(missionService.retrieveByPlayerId(eq(playerId), any(Pageable.class))).thenReturn(Page.empty());
        when(pagingProperties.getSize()).thenReturn(20);
        when(pagingProperties.getMaxCachePage()).thenReturn(2);
        when(redisCacheService.get(eq(key))).thenReturn(null);
        when(redisCacheService.getMissions(eq(key), eq(pageNumber))).thenReturn(null);

        var result = service.load(playerId, pageNumber);

        Assertions.assertEquals(result.missions(), emptyList());
        Assertions.assertEquals(result.status(), player.getStatus());
        Assertions.assertEquals(result.username(), player.getUsername());
        verify(missionService, times(1))
                .retrieveByPlayerId(anyLong(), any(Pageable.class));
        verify(playerService, times(1)).retrieve(anyLong());
        verify(redisCacheService, never()).saveMissions(anyString(), anyList(), anyInt());
        verify(redisCacheService, times(1)).save(anyString(), any());
    }


    @Test
    @DisplayName("Success -> Testing loading cached Player")
    void load_success_cached() {
        var playerId = 1L;
        var player = EntityFactory.player(playerId);
        var pageNumber = 0;
        var key = "player:%s:missions".formatted(playerId);
        when(redisCacheService.get(eq(key))).thenReturn(player);
        when(redisCacheService.getMissions(eq(key), eq(pageNumber))).thenReturn(emptyList());

        var result = service.load(playerId, pageNumber);

        Assertions.assertEquals(result.missions(), emptyList());
        Assertions.assertEquals(result.status(), player.getStatus());
        Assertions.assertEquals(result.username(), player.getUsername());
        verify(redisCacheService, never()).saveMissions(anyString(), anyList(), anyInt());
        verify(redisCacheService, never()).save(anyString(), any());
        verify(missionService, never()).retrieveByPlayerId(anyLong(), any());
        verify(playerService, never()).retrieve(anyLong());
    }


    @Test
    @DisplayName("Success -> Testing update with cached Player and different PlayerRequest")
    void update_success_cached() {
        var playerId = 1L;
        var player = EntityFactory.player(playerId);
        var playerRequest = EntityFactory.playerRequest();
        var pageNumber = 0;
        var key = "player:%s:missions".formatted(playerId);

        when(redisCacheService.getMissions(eq(key), eq(pageNumber))).thenReturn(emptyList());
        when(redisCacheService.get(eq(key))).thenReturn(player);
        when(playerService.retrieve(eq(playerId))).thenReturn(player);
        var result = service.update(playerId, playerRequest);

        Assertions.assertEquals(playerId, result.id());
        Assertions.assertEquals(result.username(), playerRequest.username());
        Assertions.assertEquals(result.status(), playerRequest.status());
        verify(redisCacheService, times(1)).save(anyString(), any());
        verify(redisCacheService, never()).saveMissions(anyString(), anyList(), anyInt());
    }


    @Test
    @DisplayName("Success -> Testing update with cached Player and same PlayerRequest's data as Player's data")
    void update_success_cached_same() {
        var playerId = 1L;
        var player = EntityFactory.player(playerId);
        var pageNumber = 0;
        var key = "player:%s:missions".formatted(playerId);
        var playerRequest = PlayerRequestDto.builder()
                .status(player.getStatus())
                .username(player.getUsername())
                .build();

        when(redisCacheService.getMissions(eq(key), eq(pageNumber))).thenReturn(emptyList());
        when(redisCacheService.get(key)).thenReturn(player);
        when(playerService.retrieve(eq(playerId))).thenReturn(player);


        var result = service.update(playerId, playerRequest);

        Assertions.assertEquals(playerId, result.id());
        Assertions.assertEquals(result.username(), player.getUsername());
        Assertions.assertEquals(result.status(), player.getStatus());
        Assertions.assertEquals(result.updatedAt().truncatedTo(ChronoUnit.MILLIS),
                player.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));

        verify(redisCacheService, never()).save(anyString(), any());
        verify(redisCacheService, never()).saveMissions(anyString(), anyList(), anyInt());
    }

    @Test
    @DisplayName("Success -> Testing update with non-cached Player and PlayerRequest are different")
    void update_success_nonCached() {
        var playerId = 1L;
        var player = EntityFactory.player(playerId);
        var playerRequest = EntityFactory.playerRequest();
        var pageNumber = 0;
        var key = "player:%s:missions".formatted(playerId);

        when(redisCacheService.getMissions(eq(key), eq(pageNumber))).thenReturn(null);
        when(redisCacheService.get(eq(key))).thenReturn(null);
        when(pagingProperties.getSize()).thenReturn(20);
        when(pagingProperties.getMaxCachePage()).thenReturn(2);
        when(missionService.retrieveByPlayerId(eq(playerId),any(Pageable.class))).thenReturn(Page.empty());
        when(playerService.retrieve(eq(playerId))).thenReturn(player);

        var result = service.update(playerId, playerRequest);

        Assertions.assertEquals(playerId, result.id());
        Assertions.assertEquals(result.username(), player.getUsername());
        Assertions.assertEquals(result.status(), player.getStatus());
        Assertions.assertEquals(result.updatedAt().truncatedTo(ChronoUnit.MILLIS),
                player.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
        verify(redisCacheService,times(1)).save(anyString(),any());
    }

    @Test
    @DisplayName("Success -> Testing update with non-cached Player and PlayerRequest are same")
    void update_success_nonCached_same() {
        var playerId = 1L;
        var player = EntityFactory.player(playerId);
        var playerRequest = PlayerRequestDto.builder()
                .username(player.getUsername())
                .status(player.getStatus())
                .build();
        var pageNumber = 0;
        var key = "player:%s:missions".formatted(playerId);

        when(redisCacheService.getMissions(eq(key), eq(pageNumber))).thenReturn(null);
        when(redisCacheService.get(eq(key))).thenReturn(null);
        when(pagingProperties.getSize()).thenReturn(20);
        when(pagingProperties.getMaxCachePage()).thenReturn(2);
        when(missionService.retrieveByPlayerId(eq(playerId),any(Pageable.class))).thenReturn(Page.empty());
        when(playerService.retrieve(eq(playerId))).thenReturn(player);

        var result = service.update(playerId, playerRequest);

        Assertions.assertEquals(playerId, result.id());
        Assertions.assertEquals(result.username(), player.getUsername());
        Assertions.assertEquals(result.status(), player.getStatus());
        Assertions.assertEquals(result.updatedAt().truncatedTo(ChronoUnit.MILLIS),
                player.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
        verify(redisCacheService,times(1)).save(anyString(),any());
    }
}