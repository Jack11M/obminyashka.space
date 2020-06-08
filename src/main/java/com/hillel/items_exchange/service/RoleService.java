package com.hillel.items_exchange.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.hillel.items_exchange.dao.RoleRepository;
import com.hillel.items_exchange.model.Role;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
