package space.obminyashka.items_exchange.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.authorization.jwt.JwtTokenFilter;
import space.obminyashka.items_exchange.authorization.oauth2.OAuthLoginSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.ttf",
                        "/**/*.chunk.*").permitAll()
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs", "/webjars/**", "/actuator/health", "/error").permitAll()
                .antMatchers(HttpMethod.POST, ApiKey.OAUTH2, ApiKey.OAUTH2_LOGIN).permitAll()
                .antMatchers(HttpMethod.POST, ApiKey.AUTH_LOGIN, ApiKey.AUTH_REGISTER, ApiKey.AUTH_REFRESH_TOKEN).permitAll()
                .antMatchers(HttpMethod.GET,
                        ApiKey.FRONT_LOGIN,
                        ApiKey.FRONT_SIGN,
                        ApiKey.FRONT_USER,
                        ApiKey.FRONT_ADV_ADD,
                        ApiKey.FRONT_PRODUCT,
                        ApiKey.FRONT_FILTER).permitAll()
                .antMatchers(HttpMethod.GET,
                        ApiKey.OAUTH2_SUCCESS,
                        ApiKey.ADV + "/**",
                        ApiKey.IMAGE + "/**",
                        ApiKey.CATEGORY + "/**",
                        ApiKey.SUBCATEGORY + "/**",
                        ApiKey.LOCATION + "/**").permitAll()
                .antMatchers(ApiKey.AUTH_OAUTH2_SUCCESS, ApiKey.AUTH_LOGOUT, ApiKey.USER_MY_INFO, ApiKey.USER_MY_ADV).authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().successHandler(oauthLoginSuccessHandler)
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler)
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and()
                .build();
    }
}
