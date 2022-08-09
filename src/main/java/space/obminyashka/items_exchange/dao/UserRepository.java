package space.obminyashka.items_exchange.dao;

import space.obminyashka.items_exchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailOrUsername(String username, String email);

    boolean existsByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByRefreshToken_Token(String token);
}
