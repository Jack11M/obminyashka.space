package space.obminyashka.items_exchange.dao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import space.obminyashka.items_exchange.model.RefreshToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into refresh_token(id, user_id, token, expiry_date) " +
            "values(:id, (select id from user where username = :username), :token, :expiryDate)")
    void createRefreshToken(UUID id, String username, String token, LocalDateTime expiryDate);

    default void createRefreshToken(String username, String token, LocalDateTime expiryDate) {
        this.createRefreshToken(UUID.randomUUID(), username, token, expiryDate);
    }

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update refresh_token set token = :token, expiry_date = :expiryDate " +
            "where user_id = (select id from user where username = :username)")
    void updateRefreshToken(String username, String token, LocalDateTime expiryDate);

    void deleteByUserUsername(String username);
}
