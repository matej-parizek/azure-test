package com.cgi.parizek.matej.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record MissionDto(
        Long id,
        String type,
        String name,
        String description,
        boolean completed,
        int progress,
        int requiredProgress,
        LocalDateTime updatedAt,
        List<MissionRewardDto> rewards,
        Set<TagDto> tags
) {}
