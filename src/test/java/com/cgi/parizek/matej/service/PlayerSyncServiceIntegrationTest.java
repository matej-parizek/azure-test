package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.EntityFactory;
import com.cgi.parizek.matej.TestRedisConfiguration;
import com.cgi.parizek.matej.redis.PlayerCache;
import com.cgi.parizek.matej.repository.IPlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRedisConfiguration.class)
public class PlayerSyncServiceIntegrationTest {
    private final static String PLAYER_CACHE = "player:%s:missions";

    @Autowired
    IPlayerRepository playerRepository;

    @Autowired
    PlayerCache playerCache;

    @Autowired
    IPlayerSyncService service;

    @Test
    public void load_success_without_cache() {
        var player = playerRepository.save(EntityFactory.player());
        IntStream.range(2, 10).asLongStream().forEach(i ->
                playerRepository.save(EntityFactory.player())
        );
        var result = service.load(player.getId(),1);

        Assertions.assertEquals(result.getId(), player.getId());
        Assertions.assertEquals(result.getUsername(), player.getUsername());
        Assertions.assertEquals(result.getStatus(), player.getStatus());
        Assertions.assertNotNull(result.getProfile());
        Assertions.assertNotNull(player.getProfile());
        Assertions.assertEquals(result.getProfile().getPlayerId(), player.getProfile().getPlayerId());
        Assertions.assertEquals(result.getProfile().getAge(), player.getProfile().getAge());
        Assertions.assertEquals(result.getProfile().getBio(), player.getProfile().getBio());

        var cached = playerCache.get(PLAYER_CACHE.formatted(player.getId()));

        Assertions.assertNotNull(cached);
        Assertions.assertEquals(result.getUsername(), cached.getUsername());
        Assertions.assertEquals(result.getStatus(), cached.getStatus());
        Assertions.assertNotNull(result.getProfile());
        Assertions.assertNotNull(cached.getProfile());
        Assertions.assertEquals(result.getProfile().getPlayerId(), cached.getProfile().getPlayerId());
        Assertions.assertEquals(result.getProfile().getAge(), cached.getProfile().getAge());
        Assertions.assertEquals(result.getProfile().getBio(), cached.getProfile().getBio());
    }


    @Test
    public void update_success_without_cache(){
        var player = playerRepository.save(EntityFactory.player());
        var request = EntityFactory.playerRequest();

        var update = service.update(player.getId(),request);

        var entityOpt = playerRepository.findById(player.getId());
        var entity = entityOpt.orElseThrow(RuntimeException::new);

        Assertions.assertEquals(update.getUsername(),entity.getUsername());
        Assertions.assertEquals(update.getStatus(),entity.getStatus());
        Assertions.assertNotEquals(entity.getUpdatedAt(),player.getUpdatedAt());
        Assertions.assertNotNull(update.getUpdatedAt());
        Assertions.assertEquals(update.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),entity.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertNotNull(update.getProfile());
        Assertions.assertNotNull(entity.getProfile());
        Assertions.assertEquals(update.getProfile().getBio(),entity.getProfile().getBio());
        Assertions.assertEquals(update.getProfile().getCountry(),entity.getProfile().getCountry());
        Assertions.assertEquals(update.getProfile().getUpdatedAt(),entity.getProfile().getUpdatedAt());
    }
}
