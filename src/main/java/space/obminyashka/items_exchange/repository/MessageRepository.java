package space.obminyashka.items_exchange.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.obminyashka.items_exchange.repository.model.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId> {
}
