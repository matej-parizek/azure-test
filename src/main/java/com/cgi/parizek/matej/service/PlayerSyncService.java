package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.config.PagingProperties;
import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.PlayerDto;
import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.entity.Player;
import com.cgi.parizek.matej.mapper.MissionMapper;
import com.cgi.parizek.matej.mapper.PlayerMapper;
import com.cgi.parizek.matej.redis.IPlayerCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PlayerSyncService implements IPlayerSyncService {

    private final IMissionService missionService;
    private final IPlayerService playerService;
    private final IPlayerCache cacheService;

    /**
     * Caching player:{id}:missions
     */
    private final static String PLAYER_CACHE = "player:%s:missions";
    private final PagingProperties pagingProperties;

    @Override
    public PlayerDto load(Long playerId, Integer pageNumber) {
        var player = retrivePlayerAndSave(playerId);
        var missions = retriveMissionsAndSave(playerId, pageNumber);
        player.missions(missions);
        return player;
    }

    @Override
    @Transactional
    public PlayerDto update(Long playerId, PlayerRequestDto body) {
        var key = PLAYER_CACHE.formatted(playerId);
        var cached = cacheService.get(key);
        var player = playerService.retrieve(playerId);
        var missions = retriveMissionsAndSave(playerId, 0);
        var compare = compare(player, body);

        player.setStatus(body.getUsername());
        player.setUsername(body.getStatus());

        if (!compare || cached == null)
            cacheService.save(PLAYER_CACHE.formatted(playerId), PlayerMapper.map(player));

        return PlayerMapper.map(player, missions);
    }

    /**
     * Comparator for player data
     *
     * @param curr - current Player
     * @param next - new Player
     * @return - true if equals
     */
    private boolean compare(Player curr, PlayerRequestDto next) {
        return curr.getUsername().equals(next.getUsername()) && curr.getStatus().equals(next.getStatus());
    }

    /**
     * Method for retrive cached player if exist or find him in database
     */
    private PlayerDto retrivePlayer(PlayerDto cached, Long id) {
        return cached != null
                ? cached
                : PlayerMapper.map(playerService.retrieve(id));
    }

    /**
     * Retrieving player if not cached save him into cache
     *
     * @param id - player id
     * @return cached {@link Player}
     */
    private PlayerDto retrivePlayerAndSave(Long id) {
        var key = PLAYER_CACHE.formatted(id);
        var cachePlayer = cacheService.get(key);

        var player = retrivePlayer(cachePlayer, id);
        if (cachePlayer == null)
            cacheService.save(key, player);
        return player;
    }

    /**
     * Method for retrieving cached list of missions for player or find it into database
     *
     * @param cached     - cached mission page or null
     * @param id         - player id
     * @param pageNumber - page number
     * @return {@link List} of missions
     */
    private List<MissionDto> retrieveMissions(List<MissionDto> cached, Long id, Integer pageNumber) {
        return cached != null
                ? cached
                : retrieveMissions(id, pageNumber).toList();
    }

    /**
     * Retrive missions from page and save them into cache
     *
     * @param playerId - Player ID
     * @return Cached List of missions
     */
    private List<MissionDto> retriveMissionsAndSave(Long playerId, Integer pageNumber) {
        var key = PLAYER_CACHE.formatted(playerId);
        var cachedMissions = cacheService.getMissions(key, pageNumber);
        var missions = retrieveMissions(cachedMissions, playerId, pageNumber);
        if (pageNumber <= pagingProperties.getMaxCachePage() && cachedMissions == null)
            cacheService.saveMissions(key, missions, pageNumber);
        return missions;
    }

    /**
     * Retrive missions from database using Paging ({@link PageRequest})
     *
     * @param playerId - player id
     * @param page     - page number
     * @return {@link Stream}
     */
    private Stream<MissionDto> retrieveMissions(Long playerId, Integer page) {
        var pageRequest = PageRequest.of(page, pagingProperties.getSize());
        var missions = missionService.retrieveByPlayerId(playerId, pageRequest);
        return missions.stream().map(MissionMapper::map);
    }
}
