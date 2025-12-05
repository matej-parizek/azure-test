package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.dto.PlayerDTO;
import com.cgi.parizek.matej.mapper.MissionMapper;
import com.cgi.parizek.matej.mapper.PlayerMapper;
import com.cgi.parizek.matej.redis.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerSyncService implements IPlayerSyncService {

    private final IMissionService missionService;
    private final IPlayerService playerService;
    private final RedisCacheService<PlayerDTO> redisCacheService;
    private final static String PLAYER_CACHE = "player:%s:missions";
    @Override
    public PlayerDTO load(Long playerId) {
        var cache = redisCacheService.get(PLAYER_CACHE.formatted(playerId));
        if(cache != null){
            return cache;
        }
        var missions = missionService.retrieveByPlayerId(playerId);
        var player = playerService.retrieve(playerId);
        var missionDto = missions.stream().map(MissionMapper::mapMission).toList();
        var result = PlayerMapper.mapPlayerDto(player, missionDto);
        redisCacheService.save(PLAYER_CACHE.formatted(playerId),result);
        return result;
    }
}
