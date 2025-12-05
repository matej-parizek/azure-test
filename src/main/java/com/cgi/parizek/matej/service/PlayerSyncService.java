package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.dto.PlayerDto;
import com.cgi.parizek.matej.mapper.MissionMapper;
import com.cgi.parizek.matej.mapper.PlayerMapper;
import com.cgi.parizek.matej.redis.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerSyncService implements IPlayerSyncService {

    private final IMissionService missionService;
    private final IPlayerService playerService;
    private final CacheService<PlayerDto> redisCacheService;
    private final static String PLAYER_CACHE = "player:%s:missions";
    private final static String PLAYER_CACHE_PAGEABLE = "player:%s:missions:%s:page";
    private final PagingProperties pagingProperties;

    @Override
    public PlayerDto load(Long playerId) {
        var cache = redisCacheService.get(PLAYER_CACHE.formatted(playerId));
        if (cache != null) {
            return cache;
        }
        var missions = missionService.retrieveByPlayerId(playerId);
        var player = playerService.retrieve(playerId);
        var missionDto = missions.stream().map(MissionMapper::map).toList();
        var result = PlayerMapper.map(player, missionDto);
        redisCacheService.save(PLAYER_CACHE.formatted(playerId), result);
        return result;
    }

    @Override
    public PlayerDto load(Long playerId, Integer page) {
        var cache = redisCacheService.get(PLAYER_CACHE_PAGEABLE.formatted(playerId, page));
        if (cache != null) {
            return cache;
        }
        var pageRequest = PageRequest.of(page, pagingProperties.getSize());
        var missions = missionService.retrieveByPlayerId(playerId, pageRequest);
        var player = playerService.retrieve(playerId);
        var missionDto = missions.stream().map(MissionMapper::map).toList();
        var result = PlayerMapper.map(player, missionDto);
        if (page <= pagingProperties.getMaxCachePage())
            redisCacheService.save(PLAYER_CACHE.formatted(playerId), result);
        return result;
    }
}
