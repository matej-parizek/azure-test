package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.dto.PlayerDto;

public interface IPlayerSyncService {
    PlayerDto load(Long playerId);
    PlayerDto load(Long playerId, Integer page);
}
