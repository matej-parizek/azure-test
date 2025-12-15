package com.cgi.parizek.matej;

import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.MissionRewardDto;
import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.dto.TagDto;
import com.cgi.parizek.matej.entity.*;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

import java.time.LocalDateTime;

import static io.micrometer.common.util.StringUtils.truncate;

@UtilityClass
public class EntityFactory {
    private final Faker faker = new Faker();

    public Player.PlayerBuilder player(Long id) {
        var now = LocalDateTime.now();
        return Player.builder()
                .id(id)
                .username(truncate(faker.name().firstName(), 20))
                .status("ACTIVE")
                .createdAt(now)
                .updatedAt(now);
    }

    public PlayerProfile.PlayerProfileBuilder playerProfile(Long id) {
        var fullWord = faker.lorem().word();
        var word = fullWord.substring(0, Math.min(255, fullWord.length()));
        return PlayerProfile.builder()
                .playerId(id)
                .age(faker.number().numberBetween(18, 80))
                .bio(word)
                .updatedAt(LocalDateTime.now())
                .country(truncate(faker.country().name(), 50));
    }


    public PlayerRequestDto.PlayerRequestDtoBuilder playerRequest() {
        return PlayerRequestDto.builder()
                .username(truncate(faker.name().firstName(), 20))
                .status("ACTIVE");
    }

    public Mission.MissionBuilder mission(Long id) {
        var fullWord = faker.lorem().word();
        var description = fullWord.substring(0, Math.min(255, fullWord.length()));
        var type = fullWord.substring(0, Math.min(50, fullWord.length()));
        return Mission.builder()
                .id(id)
                .completed(faker.bool().bool())
                .description(description)
                .progress(faker.number().randomDigit())
                .name(truncate(faker.name().firstName(), 100))
                .type(type)
                .requiredProgress(faker.number().randomDigit());
    }

    public MissionReward.MissionRewardBuilder missionReward(Long id) {
        return MissionReward.builder()
                .id(id)
                .amount(Long.valueOf(faker.number().randomNumber()).intValue())
                .rewardType(truncate(faker.naruto().eye(), 50));
    }

    public Tag.TagBuilder tag(Long id) {
        return Tag.builder()
                .id(id)
                .name(faker.name().firstName());
    }


    public TagDto.TagDtoBuilder tagDto(Long id){
        return TagDto.builder()
                .id(id)
                .name(faker.name().firstName());
    }

    public MissionRewardDto.MissionRewardDtoBuilder missionRewardDto(Long id){
        return MissionRewardDto.builder()
                .id(id)
                .amount(faker.number().randomDigit())
                .rewardType(truncate(faker.naruto().eye(), 50));
    }

    public MissionDto.MissionDtoBuilder missionDto(Long id){
       return MissionDto.builder()
                .id(id)
                .type(faker.options().option("DAILY", "WEEKLY", "STORY", "EVENT"))
                .name(faker.esports().event())
                .description(faker.lorem().sentence())
                .completed(faker.bool().bool())
                .progress(faker.number().numberBetween(0, 50))
                .requiredProgress(faker.number().numberBetween(50, 100))
                .updatedAt(LocalDateTime.now());

    }
}
