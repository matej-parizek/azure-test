package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.PlayerProfileDto;
import com.cgi.parizek.matej.entity.PlayerProfile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerProfileMapper {

    public PlayerProfileDto map(PlayerProfile profile) {
        if (profile == null) {
            return null;
        }

        Long playerId = profile.getPlayer() != null
                ? profile.getPlayer().getId()
                : null;

        return new PlayerProfileDto(
                playerId,
                profile.getCountry(),
                profile.getAge(),
                profile.getBio(),
                profile.getUpdatedAt()
        );
    }
}
