package com.cgi.parizek.matej.dto;

import lombok.Builder;

@Builder
public record MissionRewardDto(
        Long id,
        String rewardType,
        int amount
) {}
