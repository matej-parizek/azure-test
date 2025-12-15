package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.PlayerProfileDto;
import com.cgi.parizek.matej.entity.PlayerProfile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerProfileMapper {

    public PlayerProfileDto map(PlayerProfile profile) {
        if (profile == null)
            return null;

        Long playerId = profile.getPlayer() != null
                ? profile.getPlayer().getId()
                : profile.getPlayerId();

        return PlayerProfileDto.builder()
                .playerId(playerId)
                .country(profile.getCountry())
                .age(profile.getAge())
                .bio(profile.getBio())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
