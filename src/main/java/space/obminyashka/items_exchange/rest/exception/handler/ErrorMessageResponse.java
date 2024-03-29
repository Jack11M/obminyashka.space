package space.obminyashka.items_exchange.rest.exception.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

public record ErrorMessageResponse(String error, String path, String method) {

    @Override
    public String toString() {
        return new StringJoiner(".", "Api error: ", ".")
                .add(error)
                .add(" At " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME))
                .add(" Path: " + path)
                .add(" Method: " + method)
                .toString();
    }
}
