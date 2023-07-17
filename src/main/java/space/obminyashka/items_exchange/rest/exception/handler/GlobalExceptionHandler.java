package space.obminyashka.items_exchange.rest.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import space.obminyashka.items_exchange.rest.exception.*;
import space.obminyashka.items_exchange.rest.exception.bad_request.BadRequestException;
import space.obminyashka.items_exchange.rest.exception.not_found.CategorySizeNotFoundException;
import space.obminyashka.items_exchange.rest.exception.not_found.EmailValidationCodeNotFoundException;
import space.obminyashka.items_exchange.rest.exception.not_found.EntityIdNotFoundException;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleValidationExceptions(MethodArgumentNotValidException e, ServletWebRequest request) {
        final var validationErrors = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(System.lineSeparator(), "Validation error(s): ", ""));

        final var errorMessage = new ErrorMessageResponse(validationErrors, request.getRequest().getRequestURI(), request.getHttpMethod().name());
        log.warn(errorMessage.toString());
        return errorMessage;
    }

    @ExceptionHandler({BadRequestException.class, ServletException.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleRuntimeException(RuntimeException e, ServletWebRequest request) {
        if (e instanceof AccessDeniedException accessDeniedException) {
            throw accessDeniedException;
        }
        return logAndGetErrorMessage(request, e, Level.ERROR);
    }

    @ExceptionHandler({JwtException.class, RefreshTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessageResponse handleUnauthorizedExceptions(Exception e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.ERROR);
    }

    @ExceptionHandler(IllegalOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessageResponse handleForbiddenExceptions(Exception e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.ERROR);
    }

    @ExceptionHandler({
            UsernameNotFoundException.class,
            EntityNotFoundException.class,
            CategorySizeNotFoundException.class,
            EmailValidationCodeNotFoundException.class,
            EntityIdNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundExceptions(Exception e, ServletWebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(logAndGetErrorMessage(request, e, Level.WARN));
    }

    @ExceptionHandler({ElementsNumberExceedException.class, IOException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessageResponse handleIOException(Exception ex, ServletWebRequest request) {
        return logAndGetErrorMessage(request, ex, Level.ERROR);
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessageResponse handleConflictExceptions(Exception e, ServletWebRequest request) {
        return logAndGetErrorMessage(request, e, Level.WARN);
    }

    @ExceptionHandler(EmailSendingException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorMessageResponse handleEmailException(Exception ex, ServletWebRequest request) {
        return logAndGetErrorMessage(request, ex, Level.ERROR);
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public ResponseEntity<ErrorMessageResponse> handleSneakyThrownException(UndeclaredThrowableException ex, ServletWebRequest request) {
        final var cause = ex.getCause();
        if (cause instanceof UnsupportedMediaTypeException e) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(logAndGetErrorMessage(request, e, Level.WARN));
        } else if (cause instanceof IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(logAndGetErrorMessage(request, e, Level.WARN));
        }
        return ResponseEntity.internalServerError().body(logAndGetErrorMessage(request, ex, Level.ERROR));
    }

    private ErrorMessageResponse logAndGetErrorMessage(ServletWebRequest request, Exception e, Level level) {
        return logAndGetErrorMessage(request, e.getLocalizedMessage(), e, level);
    }

    private ErrorMessageResponse logAndGetErrorMessage(ServletWebRequest request, String message, Exception e, Level level) {
        if (e instanceof UndeclaredThrowableException) {
            e = (Exception) ((UndeclaredThrowableException) e).getUndeclaredThrowable();
        }
        var errorMessage = new ErrorMessageResponse(message, request.getRequest().getRequestURI(), request.getHttpMethod().name());
        log.atLevel(level)
                .setMessage("[GlobalExceptionHandler] ".concat(errorMessage.toString()))
                .setCause(e)
                .log();
        return errorMessage;
    }
}
