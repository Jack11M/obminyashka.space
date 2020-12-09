package com.hillel.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.ChatRepository;
import com.hillel.items_exchange.dao.MessageRepository;
import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.model.Attachment;
import com.hillel.items_exchange.model.Chat;
import com.hillel.items_exchange.model.Message;
import com.hillel.items_exchange.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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
    @DataSet("database_init.yml")
    void shouldAddNewMessageWithAttachmentToExistingChat() {
        Chat existingChat = chatRepository.findByHash(EXISTING_CHAT_HASH).orElseThrow(EntityNotFoundException::new);
        User user = existingChat.getUsers().stream()
                .filter(u -> u.getUsername().equals("admin"))
                .findAny().orElseThrow(EntityNotFoundException::new);
        existingChat.getMessages().add(createMessage(existingChat, user));
        chatRepository.saveAndFlush(existingChat);
        final Message savedMessage =
                existingChat.getMessages().stream()
                        .filter(message -> message.getText().equals(TEST_MESSAGE_TEXT))
                        .findAny().orElseThrow(EntityNotFoundException::new);
        assertEquals(2, messageRepository.count());
        assertNotEquals(0L, savedMessage.getId(), "Saved message ID must be greater than 0");
        assertTrue(messageRepository.findById(savedMessage.getId()).isPresent());
        assertEquals(TEST_MESSAGE_TEXT, messageRepository.findById(savedMessage.getId()).get().getText());
        assertEquals(1, savedMessage.getAttachments().size());
        assertTrue(savedMessage.getAttachments().stream().findFirst().isPresent());
        final Attachment savedAttachment = savedMessage.getAttachments().stream().findFirst().get();
        assertNotEquals(0L, savedAttachment.getId(), "Saved attachment ID must be greater than 0");
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
