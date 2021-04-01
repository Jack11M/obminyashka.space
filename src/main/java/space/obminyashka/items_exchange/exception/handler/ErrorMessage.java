package space.obminyashka.items_exchange.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.StringJoiner;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private String timestamp;
    private String error;
    private String path;
    private HttpMethod method;

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(".", "Api error: ", ".");
        joiner.add(error);
        joiner.add(" At " + timestamp);
        joiner.add(" Path: " + path);
        joiner.add(" Method: " + method);

        return joiner.toString();
    }
}
