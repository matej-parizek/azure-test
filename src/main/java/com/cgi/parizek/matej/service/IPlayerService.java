package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.entity.Player;

public interface IPlayerService {
    Player retrieve(Long playerId);
    Player update(Long playerId, PlayerRequestDto body);
}
