package com.hillel.items_exchange.util.exceptionshandling;

import com.hillel.items_exchange.exception.InvalidDtoException;
import com.hillel.items_exchange.util.MessageSourceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

@ControllerAdvice
@Log4j2
@AllArgsConstructor
public class GlobalControllerExceptionHandler {

    private final MessageSourceUtil messageSourceUtil;

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UsernameNotFoundException e,
                                                              ServletWebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.NOT_FOUND.value(),
                messageSourceUtil.getExceptionMessageSource("user.not-found.exception"),
                e.getMessage(), request.getRequest().getRequestURI(), request.getHttpMethod());

        log.warn(errorMessage.errorLog());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleSecurityException(SecurityException e,
                                                          ServletWebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.CONFLICT.value(),
                messageSourceUtil.getExceptionMessageSource("security.exception"),
                e.getMessage(), request.getRequest().getRequestURI(), request.getHttpMethod());

        log.info(errorMessage.errorLog());

        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e,
                                                                ServletWebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.NOT_FOUND.value(),
                messageSourceUtil.getExceptionMessageSource("entity.not-found.exception"),
                e.getLocalizedMessage(), request.getRequest().getRequestURI(), request.getHttpMethod());

        log.warn(errorMessage.errorLog());

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalIdentifierException.class)
    public ResponseEntity<Object> handleIllegalIdentifierException(IllegalIdentifierException e,
                                                                   ServletWebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
                messageSourceUtil.getExceptionMessageSource("illegal.id.exception"),
                e.getMessage(), request.getRequest().getRequestURI(), request.getHttpMethod());

        log.warn(errorMessage.errorLog());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDtoException.class)
    public ResponseEntity<Object> handleInvalidDtoException(InvalidDtoException e,
                                                            ServletWebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
                messageSourceUtil.getExceptionMessageSource("invalid.dto.exception"),
                e.getMessage(), request.getRequest().getRequestURI(), request.getHttpMethod());

        log.warn(errorMessage.errorLog());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSqlException(SQLIntegrityConstraintViolationException e,
                                                     ServletWebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
                messageSourceUtil.getExceptionMessageSource("sql.exception"),
                e.getLocalizedMessage(), request.getRequest().getRequestURI(), request.getHttpMethod());

        log.error(errorMessage.errorLog());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e,
                                                                 ServletWebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.BAD_REQUEST.value(),
                messageSourceUtil.getExceptionMessageSource("illegal.argument.exception"),
                e.getLocalizedMessage(), request.getRequest().getRequestURI(), request.getHttpMethod());

        log.warn(errorMessage.errorLog());

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
