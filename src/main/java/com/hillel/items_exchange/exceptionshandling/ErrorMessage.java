package com.hillel.items_exchange.exceptionshandling;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@ApiModel(description = "Api error model")
public class ErrorMessage {

    private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
            DateFormat.DEFAULT, Locale.getDefault());

    private String timestamp;
    private int status;
    private String error;
    private List<String> details;
    private String path;
    private HttpMethod method;

    public ErrorMessage(Date timestamp, int status, String error, List<String> details, String path, HttpMethod method) {
        this.timestamp = dateFormat.format(timestamp);
        this.status = status;
        this.error = error;
        this.details = details;
        this.path = path;
        this.method = method;
    }

    public ErrorMessage(Date timestamp, int status, String error, String detail, String path, HttpMethod method) {
        this.timestamp = dateFormat.format(timestamp);
        this.status = status;
        this.error = error;
        this.details = Collections.singletonList(detail);
        this.path = path;
        this.method = method;
    }

    public StringBuilder errorLog() {
        return new StringBuilder("Api error: ")
                .append("\"").append(this.error).append("\"").append(" at ")
                .append(this.timestamp).append(".")
                .append(" Path: ").append("\"").append(this.path).append("\"").append(".")
                .append(" Method: ").append("\"").append(this.method).append("\"").append(".");
    }
}
