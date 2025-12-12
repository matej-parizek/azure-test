package com.cgi.parizek.matej;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public abstract class AIntegrationTest {
    protected DatabaseEntityFactory factory = new DatabaseEntityFactory();
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    protected static String toJson(Object obj) {
        return MAPPER.writeValueAsString(obj);
    }

    @SneakyThrows
    protected static <T> T extract(String json, TypeReference<T> type) {
        return MAPPER.readValue(json, type);
    }

}
