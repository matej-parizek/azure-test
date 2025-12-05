package com.cgi.parizek.matej.dto;


import java.util.List;

public record MissionDTO(
        Long id,
        String type,
        String name,
        String description,
        Boolean completed,
        Integer progress,
        Integer requiredProgress,
        List<MissionRewardDTO> rewards,
        List<String>tags
) {
}
