package com.cgi.parizek.matej.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PlayerDto(
        Long id,
        String username,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        PlayerProfileDto profile,
        List<MissionDto> missions
) {}
