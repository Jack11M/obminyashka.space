package com.hillel.items_exchange.chat;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.*;
import com.hillel.items_exchange.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@DBRider
@DBUnit(cacheConnection = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChatEntitiesIntegrationTest {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AttachmentRepository attachmentRepository;

    private final String CHAT_HASH = "TestHash";
    private final String FIRST_USER_USERNAME = "test";
    private final String SECOND_USER_USERNAME = "test2";

    @Test
    @DataSet("chat/init_db.yml")
    @ExpectedDataSet(value = {"chat/init_db.yml", "chat/add_new_chat_to_user.yml"})
    public void shouldAddNewChatToUser() {
        User user = getExistingUserByUsername(SECOND_USER_USERNAME);
        Chat chat = new Chat(0L, CHAT_HASH, getExistingAdvertisement(),
                new ArrayList<>(List.of(user)), Collections.emptyList());
        user.getChats().add(chat);
        userRepository.saveAndFlush(user);
    }

    @Test
    @DataSet(value = {"chat/init_db.yml", "chat/add_new_chat_to_user.yml"})
    @ExpectedDataSet(value = {"chat/init_db.yml", "chat/add_user_to_existing_chat.yml"},
            ignoreCols = "ID")
    public void shouldAddAnotherUserToChat() {
        final Chat chat = getExistingChat();
        final User user = getExistingUserByUsername(FIRST_USER_USERNAME);
        user.getChats().add(chat);
        userRepository.saveAndFlush(user);
    }

    @Test
    @DataSet(value = {"chat/init_db.yml", "chat/add_new_chat_to_user.yml"})
    @ExpectedDataSet(value = {"chat/init_db.yml",
            "chat/add_new_chat_to_user.yml",
            "chat/add_new_message_without_attachments_to_chat.yml"},
            ignoreCols = {"created", "updated", "id"})
    @ExportDataSet(format = DataSetFormat.YML, outputName = "target/exported/testing.yml")
    public void shouldAddNewMessageWithNoAttachmentsToUsersChat() {
        final User user = getExistingUserByUsername(SECOND_USER_USERNAME);
        final Chat chat = getExistingChat();
        Message message = createMessageWithoutAttachments(user, chat);
        chat.getMessages().add(message);
        chatRepository.saveAndFlush(chat);
    }

    @Test
    @DataSet(value = {"chat/init_db.yml", "chat/add_user_to_existing_chat.yml"})
    @ExpectedDataSet(value = {"chat/init_db.yml",
            "chat/add_user_to_existing_chat.yml",
            "chat/add_message_with_attachments_for_every_user_in_chat.yml"},
            ignoreCols = {"created", "updated", "file_content"})
    @ExportDataSet(format = DataSetFormat.YML, outputName = "target/exported/testing.yml")
    public void shouldAddNewMessageWithTwoAttachmentsForAllUsersOfChat() {
        Chat chat = getExistingChat();
        chat.getUsers().forEach(user -> {
            Message message = createMessageWithoutAttachments(user, chat);
            message.setAttachments(List.of(
                    createAttachment(message, "png"),
                    createAttachment(message, "jpg")
            ));
            user.getMessages().add(message);
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    @DataSet(value = {"chat/init_db.yml",
            "chat/add_new_chat_to_user.yml",
            "chat/add_new_message_without_attachments_to_chat.yml"})
    @ExpectedDataSet("chat/remove_user_and_chats.yml")
    @ExportDataSet(format = DataSetFormat.YML, outputName = "target/exported/testing.yml")
    public void shouldRemoveUserWithAllOfHisChatsAndMessagesWithAttachments() {
        User user = getExistingUserByUsername(SECOND_USER_USERNAME);
        userRepository.delete(user);
        userRepository.flush();
    }

    @Test
    @DataSet(value = {"chat/init_db.yml",
            "chat/add_user_to_existing_chat.yml",
            "chat/add_message_with_attachments_for_every_user_in_chat.yml"})
    @ExpectedDataSet(value = "chat/remove_user_from_chat_with_more_than_one_user.yml",
            ignoreCols = {"created", "updated", "file_content"})
    @ExportDataSet(format = DataSetFormat.YML, outputName = "target/exported/testing.yml")
    public void shouldRemoveUserWithHisMessagesAndAttachmentsForThemButLeaveChat() {
        final User existingUser = getExistingUserByUsername(SECOND_USER_USERNAME);
        existingUser.getChats().remove(0);
        userRepository.delete(existingUser);
        userRepository.flush();
    }

    @Test
    @DataSet(value = {"chat/init_db.yml"})
    @ExpectedDataSet("chat/add_user_to_blacklist.yml")
    public void shouldAddUserToBlacklist() {
        final User user1 = getExistingUserByUsername(FIRST_USER_USERNAME);
        final User user2 = getExistingUserByUsername(SECOND_USER_USERNAME);
        user1.getBlacklistedUsers().add(user2);
        userRepository.saveAndFlush(user1);
    }

    private User getExistingUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    private Advertisement getExistingAdvertisement() {
        return advertisementRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
    }

    private Chat getExistingChat() {
        return chatRepository.findByHash(CHAT_HASH).orElseThrow(EntityNotFoundException::new);
    }

    private Message createMessageWithoutAttachments(User user, Chat chat) {
        return new Message(0L, chat, user, "Test message text of user " + user.getUsername(),
                Collections.emptyList(), null, null, MessageStatus.NEW);
    }

    private Attachment createAttachment(Message message, String type) {
        return new Attachment(0L, message, type, "file_content".getBytes());
    }
}