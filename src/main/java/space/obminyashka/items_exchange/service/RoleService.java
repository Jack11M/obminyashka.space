package space.obminyashka.items_exchange.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import space.obminyashka.items_exchange.repository.RoleRepository;
import space.obminyashka.items_exchange.repository.model.Role;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> getRole(String roleName) {
        return roleRepository.findByName(roleName);
    }

    public void setUserRoleToUserByUsername(String username) {
        roleRepository.setUserRoleToUserByUsername(username);
    }
}
