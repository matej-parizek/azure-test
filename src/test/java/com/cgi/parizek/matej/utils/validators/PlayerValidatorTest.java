package com.cgi.parizek.matej.utils.validators;

import com.cgi.parizek.matej.EntityFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerValidatorTest {

    @Test
    @DisplayName("Validation succeeds when body is valid")
    void validation_valid_body() {
        var dto = EntityFactory.playerRequest().build();
        assertTrue(PlayerValidator.validation(dto));
    }

    @Test
    @DisplayName("Validation succeeds when status have spaces in the end")
    void validation_username_trim() {
        String status = "a".repeat(50) + " ".repeat(10);
        var dto = EntityFactory.playerRequest().status(status).build();
        assertFalse(PlayerValidator.validation(dto));
    }

    @Test
    @DisplayName("Validation fails when body is null")
    void validation_body_null() {
        assertFalse(PlayerValidator.validation(null));
    }

    @Test
    @DisplayName("Validation fails when username is blank")
    void validation_username_blank() {
        var dto = EntityFactory.playerRequest().username("    ").build();
        assertFalse(PlayerValidator.validation(dto));
    }

    @Test
    @DisplayName("Validation fails when username exceeds 50 characters")
    void validation_username_too_long() {
        String username = "a".repeat(51);
        var dto = EntityFactory.playerRequest().username(username).build();
        assertFalse(PlayerValidator.validation(dto));
    }

    @Test
    @DisplayName("Validation fails when status is blank")
    void validation_status_blank() {
        var dto = EntityFactory.playerRequest().status("   ").build();
        assertFalse(PlayerValidator.validation(dto));
    }

    @Test
    @DisplayName("Validation fails when status exceeds 20 characters")
    void validation_status_too_long() {
        String status = "b".repeat(21);
        var dto = EntityFactory.playerRequest().status(status).build();
        assertFalse(PlayerValidator.validation(dto));
    }

    @Test
    @DisplayName("Validation succeeds when status have spaces in the end")
    void validation_status_trim() {
        String status = "b".repeat(20) + " ".repeat(10);
        var dto = EntityFactory.playerRequest().status(status).build();
        assertFalse(PlayerValidator.validation(dto));
    }
}
