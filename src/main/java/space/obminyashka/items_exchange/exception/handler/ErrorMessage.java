package space.obminyashka.items_exchange.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

@Getter
@Setter
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
