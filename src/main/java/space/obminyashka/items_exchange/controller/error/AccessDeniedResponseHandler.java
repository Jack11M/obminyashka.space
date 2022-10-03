package space.obminyashka.items_exchange.controller.error;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import space.obminyashka.items_exchange.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@Component
@RequiredArgsConstructor
public class AccessDeniedResponseHandler implements AccessDeniedHandler {
    private final UserService userService;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String responseMessage = getMessageSource("invalid.token");
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && isInSelfRemovingProcess(authentication)) {
            final var daysBeforeDeletion = userService.getDaysBeforeDeletion(authentication.getName());
            responseMessage = getMessageSource("exception.illegal.operation")
                    .concat(". ")
                    .concat(getParametrizedMessageSource("account.self.delete.request", daysBeforeDeletion));
        }
        response.getWriter().println(responseMessage);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    private static boolean isInSelfRemovingProcess(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_SELF_REMOVING"::equals);
    }
}
