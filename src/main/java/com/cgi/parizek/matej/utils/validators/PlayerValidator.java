package com.cgi.parizek.matej.utils.validators;

import com.cgi.parizek.matej.dto.PlayerRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlayerValidator {

    public boolean validation(PlayerRequestDto body) {
        return body != null &&
                !body.getUsername().isBlank() &&
                body.getUsername().length() <= 50 &&
                !body.getStatus().isBlank() &&
                body.getStatus().length() <= 20;
    }

}
