package space.obminyashka.items_exchange.authorization.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.service.JwtTokenService;
import space.obminyashka.items_exchange.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class OAuthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final @Lazy JwtTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        final var oauth2User = (DefaultOidcUser) authentication.getPrincipal();
        final var accessToken = Optional.ofNullable(userService.loginUserWithOAuth2(oauth2User))
                .map(user -> jwtTokenService.createAccessToken(user.getUsername(), user.getRole()))
                .orElse("");
        response.sendRedirect(ApiKey.OAUTH2_SUCCESS.concat("?code=" + accessToken));
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
