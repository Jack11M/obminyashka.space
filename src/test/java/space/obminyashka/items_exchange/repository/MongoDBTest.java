package space.obminyashka.items_exchange.repository;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.obminyashka.items_exchange.repository.model.Chat;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoDBTest {

    private final ChatRepository chatRepository;

    @Test
    void createNewChat_whenDataCorrect_successfully() {
        String senderId = "61731cc8-8104-49f0-b2c3-5a52e576ab28";
        String receiverId = "9a73b2d0-3528-4123-b2b1-27d61ed5afd3";

        var chat = new Chat()
                .setStartDate(LocalDateTime.now())
                .setSenderId(senderId)
                .setReceiverId(receiverId);

        Chat saveChat = chatRepository.save(chat);
        String newChatId = saveChat.getId();

        assertAll(
                () -> assertNotNull(newChatId),
                () -> assertNotNull(chatRepository.findById(new ObjectId(newChatId))),
                () -> assertEquals(saveChat.getSenderId(), senderId)
        );
    }
}
