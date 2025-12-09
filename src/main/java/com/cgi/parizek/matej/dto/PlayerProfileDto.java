package com.cgi.parizek.matej.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PlayerProfileDto(
        Long playerId,
        String country,
        Integer age,
        String bio,
        LocalDateTime updatedAt
) {}
