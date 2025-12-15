package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.MissionDto;
import com.cgi.parizek.matej.dto.MissionRewardDto;
import com.cgi.parizek.matej.dto.TagDto;
import com.cgi.parizek.matej.entity.Mission;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class MissionMapper {

    public MissionDto map(Mission mission) {
        if (mission == null)
            return null;

        List<MissionRewardDto> rewards = mission.getRewards() == null
                ? Collections.emptyList()
                : mission.getRewards().stream()
                .map(MissionRewardMapper::map)
                .toList();

        Set<TagDto> tags = mission.getTags() == null
                ? Collections.emptySet()
                : mission.getTags().stream()
                .map(TagMapper::map).collect(Collectors.toSet());

        return MissionDto.builder()
                .id(mission.getId())
                .type(mission.getType())
                .name(mission.getName())
                .description(mission.getDescription())
                .completed(mission.isCompleted())
                .progress(mission.getProgress())
                .requiredProgress(mission.getRequiredProgress())
                .updatedAt(mission.getUpdatedAt())
                .rewards(rewards)
                .tags(tags)
                .build();
    }
}
