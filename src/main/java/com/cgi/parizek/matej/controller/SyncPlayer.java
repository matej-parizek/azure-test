package com.cgi.parizek.matej.controller;

import com.cgi.parizek.matej.service.IPlayerSyncService;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncPlayer {
    private final IPlayerSyncService playerCachingService;


    @FunctionName("syncPlayer")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    route = "sync/players/{playerId}",
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            @BindingName("playerId") Long playerId,
            final ExecutionContext context
    ) {
        try {
            context.getLogger().info("Received request to sync player: " + playerId);
            if (playerId <= 0) {
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .body("Invalid player ID")
                        .build();
            }
            var dto = playerCachingService.load(playerId);
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(dto)
                    .build();

        } catch (NumberFormatException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Player ID must be a number")
                    .build();
        } catch (IllegalArgumentException e) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("Player not found")
                    .build();
        } catch (Exception e) {
            context.getLogger().severe("Unexpected error: " + e.getMessage());
            context.getLogger().info(e.toString());
            log.error("Connection error:", e);
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error")
                    .build();
        }
    }

}
