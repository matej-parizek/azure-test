package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.EntityFactory;
import com.cgi.parizek.matej.PlayerMissionsApplication;
import com.cgi.parizek.matej.TestRedisConfiguration;
import com.cgi.parizek.matej.entity.Player;
import com.cgi.parizek.matej.repository.IPlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRedisConfiguration.class)
public class PlayerSyncServiceIntegrationTest {
    @Autowired
    IPlayerRepository playerRepository;

    @Autowired
    IPlayerSyncService service;

    @Test
    public void load() {
        var player = playerRepository.save(EntityFactory.player());
        IntStream.range(2, 10).asLongStream().forEach(i ->
                playerRepository.save(EntityFactory.player())
        );
        var result = service.load(1L, 1);

        Assertions.assertEquals(result.getId(), player.getId());
        Assertions.assertEquals(result.getUsername(), player.getUsername());
        Assertions.assertEquals(result.getStatus(), player.getStatus());
        Assertions.assertNotNull(result.getProfile());
        Assertions.assertNotNull(player.getProfile());
        Assertions.assertEquals(result.getProfile().getPlayerId(), player.getProfile().getPlayerId());
        Assertions.assertEquals(result.getProfile().getAge(), player.getProfile().getAge());
        Assertions.assertEquals(result.getProfile().getBio(), player.getProfile().getBio());
    }
}
