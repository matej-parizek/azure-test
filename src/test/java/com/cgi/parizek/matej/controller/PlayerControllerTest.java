package com.cgi.parizek.matej.controller;

import com.cgi.parizek.matej.AIntegrationTest;
import com.cgi.parizek.matej.EntityFactory;
import com.cgi.parizek.matej.TestHttpRequestMessage;
import com.cgi.parizek.matej.dto.PlayerDto;
import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.repository.IPlayerRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Optional;
import java.util.logging.Logger;

import static org.mockito.Mockito.doReturn;

class PlayerControllerTest extends AIntegrationTest {

    private final String URL = "sync/players/%s";
    @Autowired
    PlayerController controller;

    @Autowired
    IPlayerRepository repository;

    @Mock
    ExecutionContext context;

    @BeforeEach
    void setUp() {
        doReturn(Logger.getGlobal()).when(context).getLogger();
    }

    @Test
    @DisplayName("Retrieve player - success")
    void retrieve_success() {
        var entity = repository.save(factory.player());
        HttpRequestMessage<Optional<String>> request =
                new TestHttpRequestMessage<>(Optional.empty(), HttpMethod.GET,
                        URI.create(URL.formatted(entity.getId())));

        var response = controller.retrieve(request, entity.getId().toString(), context);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    @DisplayName("Update player - success")
    void update_success() {
        var entity = repository.save(factory.player());
        var requestBody = EntityFactory.playerRequest().build();
        HttpRequestMessage<Optional<PlayerRequestDto>> request =
                new TestHttpRequestMessage<>(Optional.of(requestBody), HttpMethod.GET,
                        URI.create(URL.formatted(entity.getId())));

        var response = controller.update(request, entity.getId().toString(), context);
        var responseEntity = extract(response.getBody(), new TypeReference<PlayerDto>() {});
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(requestBody.getStatus(), responseEntity.getStatus());
        Assertions.assertEquals(requestBody.getUsername(), responseEntity.getUsername());
    }

    @Test
    @DisplayName("Retrieve player - not found")
    void retrieve_not_found() {
        var entity = Long.MAX_VALUE;
        HttpRequestMessage<Optional<String>> request =
                new TestHttpRequestMessage<>(Optional.empty(), HttpMethod.GET,
                        URI.create(URL.formatted(entity)));
        var response = controller.retrieve(request, String.valueOf(entity), context);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

}