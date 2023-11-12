package space.obminyashka.items_exchange.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Message;

import java.util.UUID;

@Repository
public interface MessageRepository extends MongoRepository<Message, UUID> {
}
