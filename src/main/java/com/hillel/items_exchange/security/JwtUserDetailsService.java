package com.hillel.items_exchange.security;

import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.security.jwt.JwtUser;
import com.hillel.items_exchange.security.jwt.JwtUserFactory;
import com.hillel.items_exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("IN JwtUserDetailsService (loadUserByUsername): " +
                        "user with username: " + username + " not found"));

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN JwtUserDetailsService (loadUserByUsername): user with username: {} successfully loaded", username);
        return jwtUser;
    }
}
