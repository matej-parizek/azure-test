package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.PlayerDto;
import com.cgi.parizek.matej.dto.PlayerProfileDto;
import com.cgi.parizek.matej.entity.Player;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;

@UtilityClass
public class PlayerMapper {

    public PlayerDto map(Player player, List<MissionDto> missionDto) {
        if (player == null) {
            return null;
        }

        PlayerProfileDto profileDto = PlayerProfileMapper.map(player.getProfile());

        return PlayerDto.builder()
                .id(player.getId())
                .username(player.getUsername())
                .status(player.getStatus())
                .createdAt(player.getCreatedAt())
                .updatedAt(player.getUpdatedAt())
                .profile(profileDto)
                .missions(missionDto)
                .build();
    }

    public PlayerDto map(Player player) {
        if (player == null) {
            return null;
        }

        PlayerProfileDto profileDto = PlayerProfileMapper.map(player.getProfile());

        return PlayerDto.builder()
                .id(player.getId())
                .username(player.getUsername())
                .status(player.getStatus())
                .createdAt(player.getCreatedAt())
                .updatedAt(player.getUpdatedAt())
                .profile(profileDto)
                .missions(Collections.emptyList())
                .build();
    }
}
