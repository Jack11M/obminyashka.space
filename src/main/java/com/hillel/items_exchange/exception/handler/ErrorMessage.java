package com.hillel.items_exchange.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.StringJoiner;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {

    private String timestamp;
    private int status;
    private String error;
    private List<String> details;
    private String path;
    private HttpMethod method;

    public String errorLog() {
        StringJoiner joiner = new StringJoiner(".", "Api error: ", ".");
        joiner.add(error);
        joiner.add(" At " + timestamp);
        joiner.add(" Path: " + path);
        joiner.add(" Method: " + method);

        return joiner.toString();
    }
}
