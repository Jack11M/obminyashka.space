package space.obminyashka.items_exchange.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import space.obminyashka.items_exchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailOrUsername(String username, String email);

    boolean existsByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByRefreshToken_Token(String token);

    @Transactional
    @Modifying
    @Query("update User u set u.avatarImage = null where u.username = :username")
    void cleanAvatarForUserByName(String username);
}
