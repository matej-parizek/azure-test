package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.EntityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

class PlayerProfileMapperTest {
    @Test
    @DisplayName("PlayerProfile mapper test")
    public void playerProfile_mapper_success() {
        var player = EntityFactory.playerProfile(1L).build();

        var dto = PlayerProfileMapper.map(player);

        Assertions.assertEquals(dto.getPlayerId(), player.getPlayerId());
        Assertions.assertEquals(dto.getAge(), player.getAge());
        Assertions.assertEquals(dto.getBio(), player.getBio());
        Assertions.assertEquals(dto.getCountry(), player.getCountry());
        Assertions.assertEquals(dto.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
                player.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    @DisplayName("PlayerProfile mapper null test")
    public void playerProfile_mapper_null() {
        var dto = PlayerProfileMapper.map(null);
        Assertions.assertNull(dto);
    }

}