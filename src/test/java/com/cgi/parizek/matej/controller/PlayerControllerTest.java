package com.cgi.parizek.matej.controller;

import com.cgi.parizek.matej.AIntegrationTest;
import com.cgi.parizek.matej.EntityFactory;
import com.cgi.parizek.matej.TestRedisConfiguration;
import com.cgi.parizek.matej.dto.PlayerRequestDto;
import com.cgi.parizek.matej.repository.IPlayerRepository;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRedisConfiguration.class)
class PlayerControllerTest extends AIntegrationTest {

    @Autowired
    PlayerController controller;

    @Autowired
    IPlayerRepository repository;

    @Mock
    ExecutionContext context;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Retrieve player - success")
    void retrieve_success() {
        HttpRequestMessage<Optional<String>> request = mock(HttpRequestMessage.class);
        mockRequestBuilder(request);
        var entity = repository.save(factory.player());
        var response = controller.retrieve(request, entity.getId().toString(), context);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    @DisplayName("Update player - success")
    void update_success() {
        HttpRequestMessage<Optional<PlayerRequestDto>> request = mock(HttpRequestMessage.class);
        mockRequestBuilder(request);
        var entity = repository.save(factory.player());
        var requestBody = EntityFactory.playerRequest().build();
        when(request.getBody()).thenReturn(Optional.of(requestBody));
        var response = controller.update(request, entity.getId().toString(), context);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    @DisplayName("Retrieve player - not found")
    void retrieve_not_found() {
        HttpRequestMessage<Optional<String>> request = mock(HttpRequestMessage.class);
        mockRequestBuilder(request);
        var entity = repository.count() +1;
        var response = controller.retrieve(request, String.valueOf(entity), context);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    private static void mockRequestBuilder(HttpRequestMessage<?> requestMessage) {
        when(requestMessage.createResponseBuilder(any(HttpStatus.class))).thenAnswer(invocation -> {
            HttpStatus status = invocation.getArgument(0);
            HttpResponseMessage.Builder builder = mock(HttpResponseMessage.Builder.class);
            when(builder.body(any())).thenReturn(builder);
            when(builder.header(any(), any())).thenReturn(builder);
            when(builder.build()).thenAnswer(buildInvocation -> {
                HttpResponseMessage response = mock(HttpResponseMessage.class);
                when(response.getStatus()).thenReturn(status);
                return response;
            });
            return builder;
        });
    }
}