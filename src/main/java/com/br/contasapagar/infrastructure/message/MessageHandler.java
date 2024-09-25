package com.br.contasapagar.infrastructure.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class MessageHandler {

    private final MessageSource messageSource;

    public String getMessage(String message, Object... parameters) {
        return messageSource.getMessage(message, Arrays.stream(parameters).toArray(), Locale.getDefault());
    }

    public String getMessage(String message) {
        return messageSource.getMessage(message, null, Locale.getDefault());
    }
}
