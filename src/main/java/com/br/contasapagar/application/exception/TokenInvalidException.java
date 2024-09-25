package com.br.contasapagar.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException(String message) {
        super(message);
    }
}
