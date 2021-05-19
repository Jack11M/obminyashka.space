package space.obminyashka.items_exchange.dao;

import space.obminyashka.items_exchange.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByHash(String hash);
}
