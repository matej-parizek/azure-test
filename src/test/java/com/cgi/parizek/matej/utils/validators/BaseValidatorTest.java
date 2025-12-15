package com.cgi.parizek.matej.utils.validators;

import com.cgi.parizek.matej.exceptions.InvalidParameterException;
import com.cgi.parizek.matej.exceptions.InvalidQueryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseValidatorTest {

    @Test
    @DisplayName("ParseId returns long when input is valid")
    void parse_id_valid() {
        long result = BaseValidator.parseId("123");
        assertEquals(123L, result);
    }

    @Test
    @DisplayName("ParseId trims whitespace and returns long")
    void parse_id_trimmed() {
        long result = BaseValidator.parseId("   42   ");
        assertEquals(42L, result);
    }

    @Test
    @DisplayName("ParseId throws when input is null")
    void parse_id_null() {
        assertThrows(InvalidParameterException.class,
                () -> BaseValidator.parseId(null));
    }

    @Test
    @DisplayName("ParseId throws when input is empty")
    void parse_id_empty() {
        assertThrows(InvalidParameterException.class,
                () -> BaseValidator.parseId(""));
    }

    @Test
    @DisplayName("ParseId throws when input is non-numeric")
    void parse_id_non_numeric() {
        assertThrows(InvalidParameterException.class,
                () -> BaseValidator.parseId("abc"));
    }

    // --- parsePage tests ---
    @Test
    @DisplayName("ParsePage returns int when input is valid")
    void parse_page_valid() {
        int result = BaseValidator.parsePage("5");
        assertEquals(5, result);
    }

    @Test
    @DisplayName("ParsePage trims whitespace and returns int")
    void parse_page_trimmed() {
        int result = BaseValidator.parsePage("   10   ");
        assertEquals(10, result);
    }

    @Test
    @DisplayName("ParsePage throws when input is null")
    void parse_page_null() {
        assertThrows(InvalidQueryException.class,
                () -> BaseValidator.parsePage(null));
    }

    @Test
    @DisplayName("ParsePage throws when input is empty")
    void parse_page_empty() {
        assertThrows(InvalidQueryException.class,
                () -> BaseValidator.parsePage(""));
    }

    @Test
    @DisplayName("ParsePage throws when input is non numeric")
    void parse_page_non_numeric() {
        assertThrows(InvalidQueryException.class,
                () -> BaseValidator.parsePage("xyz"));
    }

    @Test
    @DisplayName("ParsePage throws when input is non numeric")
    void parse_page_negative() {
        assertThrows(InvalidQueryException.class,
                () -> BaseValidator.parsePage("-1"));
    }
}
