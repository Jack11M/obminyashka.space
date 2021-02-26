package com.hillel.items_exchange.security.jwt;

import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.model.enums.Status;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getParametrizedMessageSource;

@RequiredArgsConstructor
public class DeletedUserFilter extends GenericFilterBean {

    private final UserService userService;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestedURL = String.valueOf(request.getRequestURL());

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();

            Optional<User> userOptional = userService.findByUsernameOrEmail(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getStatus().equals(Status.DELETED) &&
                        !isUserAllowedToDoOperation(request, requestedURL)) {
                    response.getWriter().write(getMessageSource("exception.illegal.operation")
                            .concat(". ")
                            .concat(getParametrizedMessageSource("account.deleted.first",
                                    userService.getDaysBeforeDeletion(user))));
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
                else {
                    chain.doFilter(req, resp);
                }
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    private boolean isUserAllowedToDoOperation(HttpServletRequest request, String requestedURL) {
        boolean isUserAllowedToDoOperation = false;
        if (request.getMethod().equals("PUT") && requestedURL.contains("/user/service/restore") ||
                request.getMethod().equals("GET")) {
            isUserAllowedToDoOperation = true;
        }

        return isUserAllowedToDoOperation;
    }
}
