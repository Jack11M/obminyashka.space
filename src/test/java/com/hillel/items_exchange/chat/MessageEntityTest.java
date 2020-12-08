package com.hillel.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.ChatRepository;
import com.hillel.items_exchange.dao.MessageRepository;
import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;

@DBRider
@Transactional
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:index-reset.sql")
class MessageEntityTest {

    private static final String EXISTING_CHAT_HASH = "ChatHash";
    private static final String TEST_MESSAGE_TEXT = "Test message text";

    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;

    @Test
    @Transactional
    @DataSet("chat/db_init.yml")
    @ExpectedDataSet(value = "chat/new_message.yml", ignoreCols = {"created", "updated", "file_content"})
    void shouldAddNewMessageToExistingChat() {
        Chat existingChat = chatRepository.findByHash(EXISTING_CHAT_HASH).orElseThrow(EntityNotFoundException::new);
        User user = existingChat.getUsers().stream()
                .filter(u -> u.getUsername().equals("user"))
                .findAny().orElseThrow(EntityNotFoundException::new);
        existingChat.getMessages().add(createMessage(existingChat, user));
        chatRepository.saveAndFlush(existingChat);
    }

    private Message createMessage(Chat chat, User user) {
        Message message = new Message(chat, user, TEST_MESSAGE_TEXT, new ArrayList<>());
        message.getAttachments().add(createAttachment(message));
        return message;
    }

    private Attachment createAttachment(Message message) {
        return new Attachment(0L, message, "jpg", "file-content".getBytes());
    }


}
