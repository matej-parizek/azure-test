package com.cgi.parizek.matej;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRedisConfiguration.class)
@Transactional
public abstract class AIntegrationTest {
    protected DatabaseEntityFactory factory = new DatabaseEntityFactory();
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    protected static <T> T extract(Object json, TypeReference<T> clazz) {
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return MAPPER.convertValue(json, clazz);
    }

}
