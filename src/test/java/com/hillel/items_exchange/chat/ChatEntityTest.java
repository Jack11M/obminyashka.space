package com.hillel.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.ChatRepository;
import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.Chat;
import com.hillel.items_exchange.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


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
        Advertisement advertisement = admin.getAdvertisements().stream()
                .filter(adv -> adv.getId() == 3L)
                .findAny().orElseThrow(EntityNotFoundException::new);
        Chat chat = createChat(advertisement, List.of(admin, user));
        admin.getChats().add(chat);
        userRepository.saveAndFlush(admin);
        Chat savedChat = admin.getChats().stream()
                .filter(c -> c.getHash().equals(TEST_CHAT_HASH))
                .findAny().orElseThrow(EntityNotFoundException::new);
        assertNotEquals(0L, savedChat.getId(), "Saved chat ID must be greater than 0");
        assertEquals(2, chatRepository.count());
        assertTrue(chatRepository.findByHash(TEST_CHAT_HASH).isPresent());
    }

    private Chat createChat(Advertisement advertisement, List<User> users) {
        return new Chat(0L, TEST_CHAT_HASH, advertisement, users, Collections.emptyList());
    }
}
