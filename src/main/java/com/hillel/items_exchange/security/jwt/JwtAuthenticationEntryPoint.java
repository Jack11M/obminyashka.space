package com.hillel.items_exchange.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Value("${invalid.token}")
    private String message;

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {

        final String expired = (String) httpServletRequest.getAttribute("expired");
        final String invalid = (String) httpServletRequest.getAttribute("invalid");
        final String prefixIsAbsent = (String) httpServletRequest.getAttribute("bearerIsAbsent");
        final String unauthorized = (String) httpServletRequest.getAttribute("unauthorized");

        if (expired != null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, expired);
        } else if (invalid != null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, invalid);
        } else if (prefixIsAbsent != null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, prefixIsAbsent);
        } else if (unauthorized != null) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, unauthorized);
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
        }
    }
}
