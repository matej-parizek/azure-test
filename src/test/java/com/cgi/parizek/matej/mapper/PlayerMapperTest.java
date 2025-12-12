package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.EntityFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;

class PlayerMapperTest {

    @Test
    @DisplayName("Player mapper test")
    public void player_mapper_test() {
        var player = EntityFactory.player(1L).build();
        var dto = PlayerMapper.map(player);

        Assertions.assertEquals(dto.getId(), player.getId());
        Assertions.assertEquals(dto.getStatus(), player.getStatus());
        Assertions.assertEquals(dto.getUsername(), player.getUsername());
        Assertions.assertEquals(dto.getCreatedAt().truncatedTo(ChronoUnit.MILLIS),
                player.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(dto.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS),
                player.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS));

        if (player.getProfile() != null)
            Assertions.assertNotNull(dto.getProfile());
    }


    @Test
    @DisplayName("Player mapper with dto missions")
    public void player_mapper_with_missions() {
        var mission1 = EntityFactory.missionDto(1L).build();
        var mission2 = EntityFactory.missionDto(2L).build();

        var player = EntityFactory.player(1L)
                .missions(List.of(mission1, mission2))
                .build();

        var dto = PlayerMapper.map(player, List.of(mission1, mission2));

        Assertions.assertEquals(2, dto.getMissions().size());
        Assertions.assertEquals(mission1.getId(), dto.getMissions().get(0).getId());
        Assertions.assertEquals(mission2.getId(), dto.getMissions().get(1).getId());
    }

    @Test
    @DisplayName("Player mapper null test")
    public void player_mapper_null() {
        var dto = PlayerMapper.map(null);
        Assertions.assertNull(dto);
    }

    @Test
    @DisplayName("Player mapper with missions null test")
    public void player_mapper_with_missions_null() {
        var dto = PlayerMapper.map(null, null);
        Assertions.assertNull(dto);
    }
}