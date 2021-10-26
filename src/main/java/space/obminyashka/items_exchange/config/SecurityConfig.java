package space.obminyashka.items_exchange.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import space.obminyashka.items_exchange.oauth2.OAuthLoginSuccessHandler;
import space.obminyashka.items_exchange.security.jwt.DeletedUserFilter;
import space.obminyashka.items_exchange.security.jwt.JwtAuthenticationEntryPoint;
import space.obminyashka.items_exchange.security.jwt.JwtTokenFilter;
import space.obminyashka.items_exchange.security.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
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
                        "/**/*.js")
                .permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/user/**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/v1/auth/logout").authenticated()
                .antMatchers(HttpMethod.GET, "/**", "/api/v1/**").permitAll()
                .antMatchers("/api/v1/adv/**", "/api/v1/category/**", "/api/v1/subcategory/**", "/api/v1/user/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(oauthLoginSuccessHandler)
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new DeletedUserFilter(userService), BasicAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
}
