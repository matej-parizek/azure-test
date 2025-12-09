package com.cgi.parizek.matej.dto;


import lombok.Builder;

@Builder
public record PlayerRequestDto(
    String username,
    String status
){}
