package com.cgi.parizek.matej.utils.validators;

import com.cgi.parizek.matej.dto.PlayerRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerValidator {

    public boolean validation(PlayerRequestDto body) {
        return body != null
                && body.username() != null
                && !body.username().isBlank()
                && body.username().length() <= 50
                && body.status() != null
                && !body.status().isBlank()
                && body.status().length() <= 20;
    }

}
