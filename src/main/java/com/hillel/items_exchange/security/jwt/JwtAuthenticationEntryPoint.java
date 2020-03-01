package com.hillel.items_exchange.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private MessageSource messageSource;

    @Autowired
    public JwtAuthenticationEntryPoint(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {

        final String detailedError = (String) httpServletRequest.getAttribute("detailedError");

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                Objects.requireNonNullElseGet(detailedError, () ->
                        messageSource.getMessage("invalid.token", null, Locale.US)));
    }
}
