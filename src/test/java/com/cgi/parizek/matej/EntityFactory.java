package com.cgi.parizek.matej;

import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.entity.Player;
import com.cgi.parizek.matej.entity.PlayerProfile;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

import java.time.LocalDateTime;

import static io.micrometer.common.util.StringUtils.truncate;

@UtilityClass
public class EntityFactory {
    private final Faker faker = new Faker();

    /**
     * Generated player data
     * @return {@link Player}
     */
    public Player player() {
        var now = LocalDateTime.now();
        var player = Player.builder()
                .username(truncate(faker.name().firstName(), 20))
                .status("ACTIVE")
                .createdAt(now)
                .updatedAt(now)
                .build();

        var profile = playerProfile();
        player.setProfile(profile);
        profile.setPlayer(player);

        return player;
    }

    public PlayerProfile playerProfile() {
        return PlayerProfile.builder()
                .age(faker.number().numberBetween(18, 80))
                .bio(truncate(faker.lorem().sentence(8), 255))
                .country(truncate(faker.country().name(), 50))
                .build();
    }

    public PlayerRequestDto playerRequest() {
        return PlayerRequestDto.builder()
                .username(truncate(faker.name().firstName(), 20))
                .status("ACTIVE")
                .build();
    }
}
