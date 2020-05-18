package com.hillel.items_exchange.exception.handler;

import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.util.MessageSourceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.Level.*;

@ControllerAdvice
@Log4j2
@AllArgsConstructor
public class GlobalExceptionHandler {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault());

    private final MessageSourceUtil messageSourceUtil;

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UsernameNotFoundException e,
                                                                    ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.NOT_FOUND,
                "exception.user.not-found", List.of(e.getLocalizedMessage()));

        logErrorMessage(WARN, errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorMessage> handleSecurityException(SecurityException e,
                                                          ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.CONFLICT,
                "exception.security", List.of(e.getLocalizedMessage()));

        logErrorMessage(INFO, errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException e,
                                                                ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.NOT_FOUND,
                "exception.entity.not-found", List.of(e.getLocalizedMessage()));

        logErrorMessage(WARN, errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalIdentifierException.class)
    public ResponseEntity<ErrorMessage> handleIllegalIdentifierException(IllegalIdentifierException e,
                                                                   ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.illegal.id", List.of(e.getLocalizedMessage()));

        logErrorMessage(WARN, errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDtoException.class)
    public ResponseEntity<ErrorMessage> handleInvalidDtoException(InvalidDtoException e,
                                                            ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.invalid.dto", List.of(e.getLocalizedMessage()));

        logErrorMessage(WARN, errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorMessage> handleSqlException(DataIntegrityViolationException e,
                                                     ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.sql", List.of(e.getLocalizedMessage()));

        logErrorMessage(ERROR, errorMessage);

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException e,
                                                                 ServletWebRequest request) {

        ErrorMessage errorMessage = getErrorMessage(request, HttpStatus.BAD_REQUEST,
                "exception.illegal.argument", List.of(e.getLocalizedMessage()));

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

    private ErrorMessage getErrorMessage(ServletWebRequest request, HttpStatus status,
                                         String errorMessage, List<String> details) {

        return new ErrorMessage(dateFormat.format(LocalDateTime.now()), status.value(),
                messageSourceUtil.getExceptionMessageSource(errorMessage), details,
                request.getRequest().getRequestURI(), request.getHttpMethod());
    }

    private void logErrorMessage(Level level, ErrorMessage errorMessage) {
        log.log(level, errorMessage.errorLog());
    }
}
