package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.PlayerDto;
import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.mapper.MissionMapper;
import com.cgi.parizek.matej.mapper.PlayerMapper;
import com.cgi.parizek.matej.redis.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PlayerSyncService implements IPlayerSyncService {

    private final IMissionService missionService;
    private final IPlayerService playerService;
    private final CacheService<PlayerDto> redisCacheService;
    private final static String PLAYER_CACHE = "player:%s:missions";
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
        var cache = redisCacheService.get(PLAYER_CACHE.formatted(playerId));

        if (cache != null) return cache;

        var player = playerService.retrieve(playerId);

        var missionDto = retrieveMissions(playerId, page).toList();
        var result = PlayerMapper.map(player, missionDto);

        if (page <= pagingProperties.getMaxCachePage())
            redisCacheService.save(PLAYER_CACHE.formatted(playerId), result);
        return result;
    }

    @Override
    public PlayerDto update(Long playerId, PlayerRequestDto body) {
        var cache = redisCacheService.get(PLAYER_CACHE.formatted(playerId));
        if (cache != null && compare(cache, body)) return cache;

        //Retrieve player with profile
        var updated = playerService.update(playerId, body);
        var missions = cache != null ? cache.missions() : retrieveMissions(playerId, 1).toList();
        var result = PlayerMapper.map(updated, missions);
        redisCacheService.save(PLAYER_CACHE.formatted(playerId), result);
        return result;
    }

    private boolean compare(PlayerDto curr, PlayerRequestDto next) {
        return curr.username().equals(next.username()) || curr.status().equals(next.status());
    }

    private Stream<MissionDto> retrieveMissions(Long playerId, Integer page) {
        var pageRequest = PageRequest.of(page, pagingProperties.getSize());
        var missions = missionService.retrieveByPlayerId(playerId, pageRequest);
        return missions.stream().map(MissionMapper::map);
    }
}
