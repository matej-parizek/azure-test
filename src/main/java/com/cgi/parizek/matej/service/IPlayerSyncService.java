package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.dto.PlayerDto;
import com.cgi.parizek.matej.dto.PlayerRequestDto;

public interface IPlayerSyncService {
    @Deprecated
    PlayerDto load(Long playerId);
    PlayerDto load(Long playerId, Integer page);
    PlayerDto update(Long playerId, PlayerRequestDto body);
}
