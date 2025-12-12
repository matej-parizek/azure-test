package com.cgi.parizek.matej.utils.handlers;

import com.cgi.parizek.matej.exceptions.EntityNotFoundException;
import com.cgi.parizek.matej.exceptions.InvalidBodyException;
import com.cgi.parizek.matej.exceptions.InvalidParameterException;
import com.cgi.parizek.matej.exceptions.InvalidQueryException;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PlayerHandlerException {
    /**
     * Exception handler for syncPlayer Azure function
     *
     * @param request - HTTP request
     * @param context - Azure Functions execution environment
     * @param fn      - Main logic to execute
     * @param <T>     - Type of body content
     * @return {@link HttpResponseMessage}
     */
    public <T> HttpResponseMessage handle(
            HttpRequestMessage<T> request,
            ExecutionContext context,
            FunctionExecutor fn
    ) {
        try {
            return fn.exec();
        } catch (InvalidParameterException | InvalidQueryException | InvalidBodyException e) {
            context.getLogger().severe(e.getMessage());
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage())
                    .build();
        } catch (EntityNotFoundException e) {
            context.getLogger().severe(e.getMessage());
            log.error("Entity cannot be found:", e);
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json")
                    .body(e.getMessage())
                    .build();
        } catch (Exception e) {
            context.getLogger().severe("Unexpected error: " + e.getMessage());
            log.error("Unexpected error:", e);
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body("Internal server error: " + e.getMessage())
                    .build();
        }
    }
}
