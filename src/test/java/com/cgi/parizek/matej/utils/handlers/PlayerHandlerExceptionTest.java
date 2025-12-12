package com.cgi.parizek.matej.utils.handlers;

import com.cgi.parizek.matej.exceptions.EntityNotFoundException;
import com.cgi.parizek.matej.exceptions.InvalidBodyException;
import com.cgi.parizek.matej.exceptions.InvalidParameterException;
import com.cgi.parizek.matej.exceptions.InvalidQueryException;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.nimbusds.jose.util.Pair;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerHandlerExceptionTest {
    @Mock
    FunctionExecutor functionExecutor;
    @Mock
    HttpRequestMessage<Object> requestMessage;

    @Mock
    ExecutionContext context;

    @SneakyThrows
    @ParameterizedTest(name = "Test exception handling for exception: {0}")
    @MethodSource("provideTestData")
    public void testPlayerHandlerException(Pair<RuntimeException, HttpStatus> ex) {
        var context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        when(functionExecutor.exec()).thenThrow(ex.getLeft());
        when(requestMessage.createResponseBuilder(any(HttpStatus.class))).thenAnswer(invocation -> {
            HttpStatus status = invocation.getArgument(0);
            HttpResponseMessage.Builder builder = mock(HttpResponseMessage.Builder.class);
            when(builder.body(anyString())).thenReturn(builder);
            when(builder.build()).thenAnswer(buildInvocation -> {
                HttpResponseMessage response = mock(HttpResponseMessage.class);
                when(response.getStatus()).thenReturn(status);
                return response;
            });
            return builder;
        });

        var responseMessage = PlayerHandlerException.handle(requestMessage, context, functionExecutor);

        Objects.requireNonNull(responseMessage);
        Assertions.assertEquals(responseMessage.getStatus(), ex.getRight());
    }


    static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(Pair.of(new InvalidParameterException("Invalid parameter"), HttpStatus.BAD_REQUEST)),
                Arguments.of(Pair.of(new InvalidQueryException("Query argument error"), HttpStatus.BAD_REQUEST)),
                Arguments.of(Pair.of(new InvalidBodyException("Invalid body"), HttpStatus.BAD_REQUEST)),
                Arguments.of(Pair.of(new RuntimeException("Unexpected error"), HttpStatus.INTERNAL_SERVER_ERROR)),
                Arguments.of(Pair.of(new EntityNotFoundException("Entity not found"), HttpStatus.NOT_FOUND))
        );
    }
}