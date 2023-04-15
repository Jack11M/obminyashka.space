package space.obminyashka.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import space.obminyashka.items_exchange.dao.ChatRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Chat;
import space.obminyashka.items_exchange.model.User;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DBRider
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ChatEntityTest {

    private static final String TEST_CHAT_HASH = "TestChatHash";

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DataSet("database_init.yml")
    void shouldAddNewChat() {
        User admin = userRepository.findByUsername("admin").orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUsername("user").orElseThrow(EntityNotFoundException::new);
        admin.getAdvertisements().stream()
                .filter(adv -> adv.getId().equals(UUID.fromString("4bd38c87-0f00-4375-bd8f-cd853f0eb9bd")))
                .map(adv -> createChat(adv, Set.of(admin, user)))
                .findAny()
                .ifPresent(chatRepository::save);

        assertEquals(2, chatRepository.count());
        assertTrue(chatRepository.findByHash(TEST_CHAT_HASH).isPresent());
    }

    private Chat createChat(Advertisement advertisement, Set<User> users) {
        return new Chat(TEST_CHAT_HASH, advertisement, users, Collections.emptyList());
    }
}
