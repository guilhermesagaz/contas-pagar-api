package com.br.contasapagar.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImportacaoCsvException extends RuntimeException {

    public ImportacaoCsvException(String message) {
        super(message);
    }
}
