package space.obminyashka.items_exchange.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.obminyashka.items_exchange.repository.model.Chat;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Disabled("needs an in-memory mongo solution")
class MongoDBTest {

    private final ChatRepository chatRepository;

    @Test
    void createNewChat_whenDataCorrect_successfully() {
        String senderId = "61731cc8-8104-49f0-b2c3-5a52e576ab28";
        String receiverId = "9a73b2d0-3528-4123-b2b1-27d61ed5afd3";

        var chat = new Chat()
                .setId(UUID.randomUUID())
                .setStartDate(LocalDateTime.now())
                .setSenderId(senderId)
                .setReceiverId(receiverId);

        Chat saveChat = chatRepository.save(chat);

        assertAll(
                () -> assertNotNull(chatRepository.findById(saveChat.getId())),
                () -> assertEquals(saveChat.getSenderId(), senderId)
        );
    }
}
