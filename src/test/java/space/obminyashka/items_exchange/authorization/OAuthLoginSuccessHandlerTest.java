package space.obminyashka.items_exchange.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.authorization.oauth2.OAuthLoginSuccessHandler;
import space.obminyashka.items_exchange.model.Role;
import space.obminyashka.items_exchange.model.User;
import space.obminyashka.items_exchange.service.JwtTokenService;
import space.obminyashka.items_exchange.service.UserService;


import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthLoginSuccessHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenService jwtTokenService;

    @InjectMocks
    private OAuthLoginSuccessHandler successHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Test
    void testOnAuthenticationSuccess() throws Exception {
        // Arrange
        var user = creatUser();
        var oauth2User = createDefaultOidcUser();
        String accessToken = "access_token";
        when(userService.loginUserWithOAuth2(oauth2User)).thenReturn(user);
        when(jwtTokenService.createAccessToken(user.getUsername(), user.getRole())).thenReturn(accessToken);
        when(authentication.getPrincipal()).thenReturn(oauth2User);

        // Act
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        assertAll(
                () -> verify(userService).loginUserWithOAuth2(oauth2User),
                () -> verify(jwtTokenService).createAccessToken(user.getUsername(), user.getRole()),
                () -> verify(response).sendRedirect(ApiKey.OAUTH2_SUCCESS + "?code=" + accessToken));
    }

    private DefaultOidcUser createDefaultOidcUser() {
        var roleUser = "ROLE_USER";
        String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6I";

        final var now = Instant.now();
        var idToken = new OidcIdToken(
                ID_TOKEN,
                now,
                now.plusSeconds(60),
                Map.of("groups", roleUser, "sub", 123));

        final var userInfo = new OidcUserInfo(Map.of(
                "email", "user@mail.ua",
                "given_name", "First",
                "family_name", "Last"));

        return new DefaultOidcUser(Collections.singletonList(new SimpleGrantedAuthority(roleUser)), idToken, userInfo);
    }

    private User creatUser() {
        Role role = new Role();
        role.setName("ROLE_USER");
        User user = new User();
        user.setRole(role);
        user.setUsername("First");
        return user;
    }
}