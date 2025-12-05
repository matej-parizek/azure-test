package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.dto.PlayerDTO;

public interface IPlayerSyncService {
    PlayerDTO load(Long playerId);
}
