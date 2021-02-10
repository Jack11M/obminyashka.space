package com.hillel.items_exchange.security.jwt;

import com.hillel.items_exchange.model.Role;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.model.enums.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUserFactory {

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRole()),
                isUserEnabled(user),
                user.getUpdated()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Role userRole) {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.getName()));
    }

    private static boolean isUserEnabled(User user) {
        boolean isUserEnabled = false;
        if (user.getStatus().equals(Status.ACTIVE) || user.getStatus().equals(Status.DELETED)) {
            isUserEnabled = true;
        }

        return isUserEnabled;
    }
}
