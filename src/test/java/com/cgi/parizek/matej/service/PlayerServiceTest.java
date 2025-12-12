package com.cgi.parizek.matej.service;

import com.cgi.parizek.matej.EntityFactory;
import com.cgi.parizek.matej.entity.Player;
import com.cgi.parizek.matej.exceptions.EntityNotFoundException;
import com.cgi.parizek.matej.repository.IPlayerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private IPlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    @DisplayName("Retrieve player")
    void retrieve_player() {
        Long playerId = 1L;
        Player player = EntityFactory.player(playerId).build();

        when(playerRepository.findByIdWithProfile(playerId)).thenReturn(Optional.of(player));

        Player result = playerService.retrieve(playerId);

        assertSame(player, result);

        verify(playerRepository, times(1)).findByIdWithProfile(playerId);
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    @DisplayName("Retrieve player not found")
    void retrieve_player_not_found() {
        Long playerId = 1L;

        when(playerRepository.findByIdWithProfile(playerId)).thenReturn(Optional.empty());

        try {
            playerService.retrieve(playerId);
        } catch (Exception e) {
            assertSame(EntityNotFoundException.class, e.getClass());
            assertEquals("Player with id '" + playerId + "' not found", e.getMessage());
        }

        verify(playerRepository, times(1)).findByIdWithProfile(playerId);
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    @DisplayName("Update player")
    void update_player() {
        Long playerId = 1L;
        var player = EntityFactory.player(playerId).build();
        var dto = EntityFactory.playerRequest().build();
        doNothing().when(playerRepository).flush();
        Player result = playerService.update(player, dto);
        Assertions.assertEquals(player.getUsername(), result.getUsername());
        Assertions.assertEquals(player.getStatus(), result.getStatus());
        verify(playerRepository, times(1)).flush();
        verifyNoMoreInteractions(playerRepository);
    }
}