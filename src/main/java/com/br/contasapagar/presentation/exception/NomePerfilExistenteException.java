package com.br.contasapagar.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class NomePerfilExistenteException extends RuntimeException {

    public NomePerfilExistenteException(String message) {
        super(message);
    }
}
