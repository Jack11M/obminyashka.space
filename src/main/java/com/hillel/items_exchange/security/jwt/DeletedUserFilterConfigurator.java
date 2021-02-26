package com.hillel.items_exchange.security.jwt;

import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@RequiredArgsConstructor
public class DeletedUserFilterConfigurator extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final UserService userService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        DeletedUserFilter deletedUserFilter = new DeletedUserFilter(userService);
        http.addFilterAfter(deletedUserFilter, JwtTokenFilter.class);
    }
}
