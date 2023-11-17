package space.obminyashka.items_exchange.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Chat;

import java.util.UUID;

@Repository
public interface ChatRepository extends MongoRepository<Chat, UUID> {
}
