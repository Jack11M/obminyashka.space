package com.hillel.items_exchange.config;

import com.hillel.items_exchange.security.jwt.DeletedUserFilterConfigurator;
import com.hillel.items_exchange.security.jwt.JwtAuthenticationEntryPoint;
import com.hillel.items_exchange.security.jwt.JwtConfigurator;
import com.hillel.items_exchange.security.jwt.JwtTokenProvider;
import com.hillel.items_exchange.service.UserService;
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
    public static final String HAS_ROLE_USER = "hasRole('ROLE_USER')";
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
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
                        "/**/*.js",
                        "/auth/**").permitAll()
                .antMatchers(
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/child", "/user/my-info").authenticated()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers("/adv/**", "/category/**", "/subcategory/**", "/user/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurator(jwtTokenProvider))
                .and()
                .apply(new DeletedUserFilterConfigurator(userService))
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
}
