package com.cgi.parizek.matej.mapper;

import com.cgi.parizek.matej.dto.MissionRewardDto;
import com.cgi.parizek.matej.entity.MissionReward;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MissionRewardMapper {

    public MissionRewardDto map(MissionReward reward) {
        if (reward == null)
            return null;

        return MissionRewardDto.builder()
                .id(reward.getId())
                .rewardType(reward.getRewardType())
                .amount(reward.getAmount())
                .build();
    }
}
