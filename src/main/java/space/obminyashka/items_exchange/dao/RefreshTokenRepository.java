package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import space.obminyashka.items_exchange.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserUsername(String username);
}
