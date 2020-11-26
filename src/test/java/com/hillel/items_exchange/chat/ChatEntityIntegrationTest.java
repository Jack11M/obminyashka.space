package com.hillel.items_exchange.chat;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.ChatRepository;
import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.Chat;
import com.hillel.items_exchange.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
@DBRider
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class ChatEntityIntegrationTest {

    private static final String TEST_CHAT_HASH = "TestChatHash";

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DataSet("database_init.yml")
    @ExpectedDataSet("chat/new_chat.yml")
    @ExportDataSet(format = DataSetFormat.YML, outputName = "11111.yml")
    void shouldAddNewChat() {
        Optional<User> admin = userRepository.findByUsername("admin");
        Optional<User> user = userRepository.findByUsername("user");
        assertTrue(admin.isPresent() && user.isPresent());
        Optional<Advertisement> advertisement = admin.get().getAdvertisements().stream()
                .filter(adv -> adv.getId() == 3L)
                .findAny();
        assertTrue(advertisement.isPresent());
        Chat chat = createChat(advertisement.get(), List.of(admin.get(), user.get()));
        admin.get().getChats().add(chat);
        userRepository.saveAndFlush(admin.get());
        Optional<Chat> savedChat = admin.get().getChats().stream()
                .filter(c -> c.getHash().equals(TEST_CHAT_HASH))
                .findAny();
        assertTrue(savedChat.isPresent());
        user.get().getChats().add(savedChat.get());
        userRepository.saveAndFlush(user.get());
    }

    private Chat createChat(Advertisement advertisement, List<User> users) {
        return new Chat(0L, TEST_CHAT_HASH, advertisement, users, Collections.emptyList());
    }
}
