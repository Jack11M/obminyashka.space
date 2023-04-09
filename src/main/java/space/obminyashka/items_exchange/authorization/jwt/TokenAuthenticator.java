package space.obminyashka.items_exchange.authorization.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ExceptionMessage.*;

@Component
public class TokenAuthenticator implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String responseText;
        if (authException instanceof UsernameNotFoundException) {
            responseText = getMessageSource(USER_NOT_FOUND);
        } else if (authException instanceof InsufficientAuthenticationException
                || authException instanceof BadCredentialsException) {
            responseText = authException.getLocalizedMessage();
        } else {
            responseText = "JWT is expired and needs to be recreated";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(responseText);
    }
}
