package space.obminyashka.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import space.obminyashka.items_exchange.dao.ChatRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.model.Advertisement;
import space.obminyashka.items_exchange.model.Chat;
import space.obminyashka.items_exchange.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DBRider
@SpringBootTest
@Transactional
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
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
                .filter(adv -> adv.getId() == 3L)
                .map(adv -> createChat(adv, Set.of(admin, user)))
                .findAny()
                .ifPresent(chatRepository::save);

        assertEquals(2, chatRepository.count());
        assertTrue(chatRepository.findByHash(TEST_CHAT_HASH).isPresent());
    }

    private Chat createChat(Advertisement advertisement, Set<User> users) {
        return new Chat(0L, TEST_CHAT_HASH, advertisement, users, Collections.emptyList());
    }
}
