package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.PlayerDto;
import com.cgi.parizek.matej.dto.PlayerProfileDto;
import com.cgi.parizek.matej.entity.Player;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PlayerMapper {

    public PlayerDto map(Player player, List<MissionDto> missionDto) {
        if (player == null) {
            return null;
        }

        PlayerProfileDto profileDto = PlayerProfileMapper.map(player.getProfile());

        return new PlayerDto(
                player.getId(),
                player.getUsername(),
                player.getStatus(),
                player.getCreatedAt(),
                player.getUpdatedAt(),
                profileDto,
                missionDto
        );
    }
}
