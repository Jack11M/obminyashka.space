package space.obminyashka.items_exchange.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import space.obminyashka.items_exchange.api.ApiKey;
import space.obminyashka.items_exchange.authorization.jwt.DeletedUserFilter;
import space.obminyashka.items_exchange.authorization.jwt.JwtAuthenticationEntryPoint;
import space.obminyashka.items_exchange.authorization.jwt.JwtTokenFilter;
import space.obminyashka.items_exchange.authorization.jwt.JwtTokenProvider;
import space.obminyashka.items_exchange.authorization.oauth2.OAuthLoginSuccessHandler;

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
    private final DeletedUserFilter deletedUserFilter;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public JwtTokenFilter authenticationTokenFilterBean() {
        return new JwtTokenFilter(jwtTokenProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
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
                        "/**/*.chunk.*")
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs", "/webjars/**", "/actuator/health", "/error")
                .antMatchers(HttpMethod.POST, ApiKey.OAUTH2, ApiKey.OAUTH2_LOGIN)
                .antMatchers(HttpMethod.POST, ApiKey.AUTH_LOGIN, ApiKey.AUTH_REGISTER, ApiKey.AUTH_REFRESH_TOKEN)
                .antMatchers(HttpMethod.GET, ApiKey.FRONT_LOGIN, ApiKey.FRONT_SIGN, ApiKey.FRONT_USER, ApiKey.FRONT_ADV_ADD, ApiKey.FRONT_PRODUCT,
                        ApiKey.OAUTH2_SUCCESS,
                        ApiKey.ADV + "/**",
                        ApiKey.IMAGE + "/**",
                        ApiKey.CATEGORY + "/**",
                        ApiKey.SUBCATEGORY + "/**",
                        ApiKey.LOCATION + "/**");
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
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(oauthLoginSuccessHandler)
                .and()
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(deletedUserFilter, BasicAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
    }
}
