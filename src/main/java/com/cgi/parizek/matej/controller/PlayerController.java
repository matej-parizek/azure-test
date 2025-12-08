package com.cgi.parizek.matej.controller;

import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.exceptions.InvalidBodyException;
import com.cgi.parizek.matej.service.IPlayerSyncService;
import com.cgi.parizek.matej.utils.handlers.PlayerHandlerException;
import com.cgi.parizek.matej.utils.validators.BaseValidator;
import com.cgi.parizek.matej.utils.validators.PlayerValidator;
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
public class PlayerController {
    private final IPlayerSyncService playerCachingService;


    @FunctionName("syncPlayer")
    public HttpResponseMessage retrieve(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.GET},
                    route = "sync/players/{playerId}",
                    authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            @BindingName("playerId") String playerIdStr,
            final ExecutionContext context
    ) {

        return PlayerHandlerException.handle(request, context, () -> {

            var playerId = BaseValidator.parseId(playerIdStr);
            var pageParam = request.getQueryParameters().get("page");
            int page = pageParam != null ? BaseValidator.parsePage(pageParam) : 0;

            var dto = playerCachingService.load(playerId, page);

            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(dto)
                    .build();
        });
    }

    @FunctionName("updatePlayer")
    public HttpResponseMessage update(
            @HttpTrigger(name = "req",
                    methods = {HttpMethod.PUT},
                    route = "sync/players/{playerId}",
                    authLevel = AuthorizationLevel.FUNCTION)
            HttpRequestMessage<Optional<PlayerRequestDto>> request,
            @BindingName("playerId") String playerIdStr,
            final ExecutionContext context) {
        return PlayerHandlerException.handle(request, context, () -> {
            var body = request.getBody().orElseThrow(() -> new InvalidBodyException("Body is null"));
            var playerId = BaseValidator.parseId(playerIdStr);

            if (PlayerValidator.validation(body))
                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                        .body("Invalid request body")
                        .build();

            var dto = playerCachingService.update(playerId, body);
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(dto)
                    .build();

        });
    }
}
