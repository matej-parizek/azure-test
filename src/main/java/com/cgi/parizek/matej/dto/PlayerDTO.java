package com.cgi.parizek.matej.dto;

import java.util.List;

public record PlayerDTO (
        Long id,
        String username,
        String status,
        String country,
        Integer age,
        String bio,
        List<MissionDTO> missions
){}
