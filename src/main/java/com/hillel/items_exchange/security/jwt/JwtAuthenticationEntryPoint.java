package com.hillel.items_exchange.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private ObjectMapper mapper;
    private MessageSource messageSource;

    @Autowired
    public JwtAuthenticationEntryPoint(ObjectMapper mapper, MessageSource messageSource) {
        this.mapper = mapper;
        this.messageSource = messageSource;
    }

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {

        try (ServletServerHttpResponse res = new ServletServerHttpResponse(httpServletResponse)) {
            res.setStatusCode(HttpStatus.UNAUTHORIZED);
            res.getServletResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            res.getBody().write(mapper.writeValueAsString(
                    messageSource.getMessage("invalid.token", null, Locale.US)).getBytes());
        }
    }
}
