package com.br.contasapagar.shared.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateFutureForPostValidator.class)
public @interface LocalDateFutureForPostValid {

    String message() default "Data inválida, informe uma data futura";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
