package space.obminyashka.items_exchange.security.jwt;

import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Optional;
import java.util.function.Predicate;

import static space.obminyashka.items_exchange.util.MessageSourceUtil.getMessageSource;
import static space.obminyashka.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@Component
@RequiredArgsConstructor
public class DeletedUserFilter extends GenericFilterBean {

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Principal principal = httpServletRequest.getUserPrincipal();
        if (principal != null) {

            String username = principal.getName();
            Optional<User> selfDeletedUser = userService.findByUsernameOrEmail(username)
                    .filter(Predicate.not(User::isEnabled));
            if (selfDeletedUser.isPresent() && !isAllowedMethodsAccessed(httpServletRequest)) {
                blockFurtherAccessWithError(selfDeletedUser.get(), (HttpServletResponse) servletResponse);
                return;
            }
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    private void blockFurtherAccessWithError(User user, HttpServletResponse response) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            writer.write(getMessageSource("exception.illegal.operation")
                    .concat(". ")
                    .concat(getParametrizedMessageSource("account.self.delete.request",
                            userService.getDaysBeforeDeletion(user))));
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private boolean isAllowedMethodsAccessed(HttpServletRequest request) {
        String requestedURL = request.getRequestURL().toString();
        String requestMethod = request.getMethod();

        return HttpMethod.PUT.matches(requestMethod) && requestedURL.contains("/user/service/restore") ||
                HttpMethod.GET.matches(requestMethod);
    }
}
