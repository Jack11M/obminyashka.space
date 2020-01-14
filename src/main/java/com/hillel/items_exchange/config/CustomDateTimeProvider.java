package com.hillel.items_exchange.config;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component("dateTimeProvider")
public class CustomDateTimeProvider implements DateTimeProvider {
    @Override
    @NotNull
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(LocalDate.now());
    }
}
