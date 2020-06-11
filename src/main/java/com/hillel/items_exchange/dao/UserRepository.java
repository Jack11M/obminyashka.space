package com.hillel.items_exchange.dao;

import com.hillel.items_exchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByEmailOrUsername(String username, String email);

    boolean existsByUsernameOrEmailAndPassword(String username, String email, String encryptedPassword);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String usernameOrEmail);
}
