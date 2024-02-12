package space.obminyashka.items_exchange.repository.model.base;

import lombok.NonNull;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component("dateTimeProvider")
public class CustomDateTimeProvider implements DateTimeProvider {
    @Override
    public @NonNull Optional<TemporalAccessor> getNow() {
        return Optional.of(LocalDateTime.now());
    }
}
