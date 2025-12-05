package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.MissionDTO;
import com.cgi.parizek.matej.dto.MissionRewardDTO;
import com.cgi.parizek.matej.entity.Mission;
import com.cgi.parizek.matej.entity.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MissionMapper {
    public MissionDTO mapMission(Mission mission) {
        return new MissionDTO(
                mission.getId(),
                mission.getType(),
                mission.getName(),
                mission.getDescription(),
                mission.isCompleted(),
                mission.getProgress(),
                mission.getRequiredProgress(),
                mission.getRewards().stream()
                        .map(r -> new MissionRewardDTO(r.getRewardType(), r.getAmount())).toList(),
                mission.getTags().stream().map(Tag::getName).toList()
        );
    }
}
