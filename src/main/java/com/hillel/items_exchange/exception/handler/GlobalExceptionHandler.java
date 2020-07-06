package com.hillel.items_exchange.exception.handler;

import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.exception.InvalidDtoException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionMessageSource;
import static org.apache.logging.log4j.Level.*;

@ControllerAdvice
@Log4j2
@AllArgsConstructor
public class GlobalExceptionHandler {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault());

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UsernameNotFoundException e,
                                                                    ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.NOT_FOUND,
                "exception.user.not-found", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(WARN, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({SecurityException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorMessage> handleSecurityException(Exception e, ServletWebRequest request) {

        HttpStatus httpStatus = e instanceof SecurityException ? HttpStatus.CONFLICT : HttpStatus.FORBIDDEN;
        ErrorMessage errorMessage = getErrorMessage(request, httpStatus,
                "exception.security", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(INFO, errorMessage);
        return new ResponseEntity<>(errorMessage, httpStatus);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException e,
                                                                ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.NOT_FOUND,
                "exception.entity.not-found", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(WARN, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalIdentifierException.class)
    public ResponseEntity<ErrorMessage> handleIllegalIdentifierException(IllegalIdentifierException e,
                                                                   ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.illegal.id", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(WARN, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDtoException.class)
    public ResponseEntity<ErrorMessage> handleInvalidDtoException(InvalidDtoException e,
                                                            ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.invalid.dto", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(WARN, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleSqlException(DataIntegrityViolationException e,
                                                     ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.sql", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(ERROR, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException e,
                                                                 ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.illegal.argument", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(WARN, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException e,
                                                                     ServletWebRequest request) {

        List<String> violations = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.validation", violations);
        logErrorMessage(WARN, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalOperationException.class)
    public ResponseEntity<ErrorMessage> handlerIllegalOperation(IllegalOperationException e,
                                                                ServletWebRequest request) {
        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.FORBIDDEN,
                "exception.illegal.operation", Collections.singletonList(e.getLocalizedMessage()));
        logErrorMessage(WARN, errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    private ErrorMessage getErrorMessage(ServletWebRequest request, HttpStatus status,
                                         String errorMessage, List<String> details) {

        return new ErrorMessage(dateFormat.format(LocalDateTime.now()), status.value(),
                getExceptionMessageSource(errorMessage), details,
                request.getRequest().getRequestURI(), request.getHttpMethod());
    }

    private void logErrorMessage(Level level, ErrorMessage errorMessage) {
        log.log(level, errorMessage.errorLog());
    }
}
