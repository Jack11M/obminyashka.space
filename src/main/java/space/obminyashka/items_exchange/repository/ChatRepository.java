package space.obminyashka.items_exchange.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Chat;

@Repository
public interface ChatRepository extends MongoRepository<Chat, ObjectId> {
}
