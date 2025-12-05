package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.entity.Player;

public interface IPlayerService {
    Player retrieve(Long playerId);
}
