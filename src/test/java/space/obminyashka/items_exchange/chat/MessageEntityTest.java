package space.obminyashka.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import space.obminyashka.items_exchange.dao.ChatRepository;
import space.obminyashka.items_exchange.dao.MessageRepository;
import space.obminyashka.items_exchange.dao.UserRepository;
import space.obminyashka.items_exchange.model.Attachment;
import space.obminyashka.items_exchange.model.Chat;
import space.obminyashka.items_exchange.model.Message;
import space.obminyashka.items_exchange.model.User;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@Transactional
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        assertTrue(messageRepository.findById(savedMessage.getId()).isPresent());
        Assertions.assertEquals(TEST_MESSAGE_TEXT, messageRepository.findById(savedMessage.getId()).get().getText());
        assertEquals(1, savedMessage.getAttachments().size());
        assertTrue(savedMessage.getAttachments().stream().findFirst().isPresent());
        assertNotNull(savedMessage.getCreated());
        assertNotNull(savedMessage.getUpdated());
        final Attachment savedAttachment = savedMessage.getAttachments().stream().findFirst().get();
        assertNotNull(savedAttachment.getId(), "Saved attachment ID must be greater than 0");
    }


    private Message createMessage(Chat chat, User user) {
        Message message = new Message(chat, user, TEST_MESSAGE_TEXT, new ArrayList<>());
        message.getAttachments().add(createAttachment(message));
        return message;
    }

    private Attachment createAttachment(Message message) {
        return new Attachment(UUID.randomUUID(), message, "jpg", "file-content".getBytes());
    }
}
