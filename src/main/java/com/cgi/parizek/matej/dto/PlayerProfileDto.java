package com.cgi.parizek.matej.dto;

import java.time.LocalDateTime;

public record PlayerProfileDto(
        Long playerId,
        String country,
        Integer age,
        String bio,
        LocalDateTime updatedAt
) {}
