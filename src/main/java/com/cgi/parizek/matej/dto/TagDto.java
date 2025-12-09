package com.cgi.parizek.matej.dto;

import lombok.Builder;

@Builder
public record TagDto(
        Long id,
        String name
) {}
