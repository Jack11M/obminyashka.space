package com.hillel.items_exchange.exception.handler;

import com.hillel.items_exchange.exception.DataConflictException;
import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.exception.InvalidLocationInitFileCreatingDataException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.UndeclaredThrowableException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@RestControllerAdvice
@Log4j2
@AllArgsConstructor
public class GlobalExceptionHandler {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault());

    @ExceptionHandler({UsernameNotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundExceptions(Exception e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.WARN);
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleDataConflictException(DataConflictException e, ServletWebRequest request) {
       return logAndGetErrorMessage(request, e, Level.INFO);
    }

    @ExceptionHandler({InvalidDtoException.class, InvalidLocationInitFileCreatingDataException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleBadRequestExceptions(Exception e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.WARN);
    }

    @ExceptionHandler({IllegalOperationException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleIllegalOperation(IllegalOperationException e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.WARN);
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleJwtException(JwtException e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleRuntimeException(RuntimeException e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.ERROR);
    }

    private ErrorMessage logAndGetErrorMessage(ServletWebRequest request, Exception e, Level level) {
        if (e instanceof UndeclaredThrowableException) {
            e = (Exception) ((UndeclaredThrowableException) e).getUndeclaredThrowable();
        }
        ErrorMessage errorMessage = new ErrorMessage(dateFormat.format(LocalDateTime.now()),
                e.getLocalizedMessage(),
                request.getRequest().getRequestURI(),
                request.getHttpMethod());
        log.log(level, errorMessage);
        return errorMessage;
    }
}
