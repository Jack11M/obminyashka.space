package space.obminyashka.items_exchange.controller.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Getter
@AllArgsConstructor
public class ErrorMessage {

    private String error;
    private String path;
    private HttpMethod method;

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
