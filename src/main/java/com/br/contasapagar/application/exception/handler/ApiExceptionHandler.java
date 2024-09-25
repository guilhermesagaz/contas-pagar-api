package com.br.contasapagar.application.exception.handler;

import com.br.contasapagar.application.exception.TokenInvalidException;
import com.br.contasapagar.infrastructure.message.MessageHandler;
import lombok.*;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.br.contasapagar.shared.constant.Constants.*;

@RequiredArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler {

    private final MessageHandler messageHandler;

    @ResponseBody
    @ExceptionHandler({TokenInvalidException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    protected ErrorResponse handleTokenInvalid(RuntimeException exception) {
    protected ErrorResponse handleTokenInvalid(final TokenInvalidException exception) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .detail(exception.getMessage())
                .message(messageHandler.getMessage(TOKEN_INVALID))
                .build();
    }

    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleViolation(RuntimeException exception) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(exception.getMessage())
                .message(messageHandler.getMessage(INVALID_ID))
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {IllegalArgumentException.class, jakarta.validation.ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleIllegalArgument(RuntimeException exception) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(exception.getMessage())
                .message(messageHandler.getMessage(PARAMETRO_INVALIDO))
                .build();
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private Integer status;
        private String detail;
        private String message;
    }
}
