package com.br.contasapagar.shared.converter;

import com.opencsv.bean.AbstractBeanField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateConverter extends AbstractBeanField<LocalDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected Object convert(String value) {
        try {
            return LocalDate.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
