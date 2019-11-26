package com.hillel.items_exchange.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
