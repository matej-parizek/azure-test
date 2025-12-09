package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.entity.Player;
import com.cgi.parizek.matej.exceptions.EntityNotFoundException;
import com.cgi.parizek.matej.repository.IPlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerService implements IPlayerService {
    private final IPlayerRepository playerRepository;

    @Override
    public Player retrieve(Long playerId) {
        return playerRepository.findByIdWithProfile(playerId)
                .orElseThrow(() -> {
                    log.error("Cannot found a player with id: {}", playerId);
                    return new EntityNotFoundException("Player with id '" + playerId + "' not found");
                });

    }
}
