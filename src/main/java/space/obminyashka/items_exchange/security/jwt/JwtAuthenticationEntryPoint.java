package space.obminyashka.items_exchange.security.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {

        final String detailedError = (String) httpServletRequest.getAttribute("detailedError");

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                Objects.requireNonNullElseGet(detailedError, () ->
                        getMessageSource("invalid.token")));
    }
}
