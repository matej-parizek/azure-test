package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.MissionDTO;
import com.cgi.parizek.matej.dto.PlayerDTO;
import com.cgi.parizek.matej.entity.Player;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PlayerMapper {
    public PlayerDTO mapPlayerDto(Player player, List<MissionDTO> allMissionsDTO) {
        var profile = player.getProfile();
        return new PlayerDTO(
                player.getId(),
                player.getUsername(),
                player.getStatus(),
                profile != null ? profile.getCountry() : null,
                profile != null && profile.getAge() != null ? profile.getAge() : 0,
                profile != null ? profile.getBio() : null,
                allMissionsDTO
        );
    }
}
