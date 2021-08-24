package space.obminyashka.items_exchange.security.jwt.refresh;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findById(long id);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUserUsername(String username);
}
