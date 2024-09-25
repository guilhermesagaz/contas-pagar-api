package com.br.contasapagar.shared.validator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LocalDateFutureForPostValidator implements ConstraintValidator<LocalDateFutureForPostValid, LocalDate> {

    private final HttpServletRequest request;

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return !HttpMethod.POST.name().equalsIgnoreCase(request.getMethod()) || localDate.isAfter(LocalDate.now());
    }
}
