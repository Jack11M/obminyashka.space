package com.hillel.items_exchange.chat;

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
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@DBRider
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

    @Test
    @DataSet("chat/init_db.yml")
    @ExpectedDataSet(value = {"chat/init_db.yml", "chat/create_chat.yml"})
    @ExportDataSet(format = DataSetFormat.YML, outputName = "target/exported/testing.yml")
    public void shouldCreateRecordsInChatTableAndUserChatTable() {
        Advertisement advertisement = getExistingAdvertisement();
        User user = getExistingUser();
        Chat chat = new Chat(null, "TestHash", advertisement, new ArrayList<>(List.of(user)), Collections.emptyList());
        chatRepository.save(chat);
        chatRepository.flush();
    }


    @Test
    @DataSet(value = {"chat/init_db.yml", "chat/create_chat.yml"})
    @ExpectedDataSet(value = {"chat/init_db.yml", "chat/create_chat.yml", "chat/add_message.yml"},
            ignoreCols = {"created", "updated", "id"})
    public void shouldCreateRecordInMessageTable() {
        final User user = getExistingUser();
        final Chat chat = user.getChats().get(0);
        Message message = new Message(null, chat, user, "Test message text",
                Collections.emptyList(), null, null, MessageStatus.NEW);
        messageRepository.save(message);
    }

    @Test
    @DataSet(value = {"chat/init_db.yml", "chat/create_chat.yml", "chat/add_message.yml"})
    @ExpectedDataSet(
            value = {"chat/init_db.yml", "chat/create_chat.yml", "chat/add_message.yml", "chat/add_attachment.yml"},
            ignoreCols = {"created", "updated", "file_content"})
    public void shouldCreateRecordInAttachmentTable() {
        final User user = getExistingUser();
        final Chat chat = user.getChats().get(0);
        Message message = chat.getMessages().get(0);
        Attachment a1 = new Attachment(null, message, "jpg", "file_content1".getBytes());
        Attachment a2 = new Attachment(null, message, "png", "file_content2".getBytes());
        attachmentRepository.saveAll(List.of(a1,a2));
    }

    @Test
    @DataSet(value = {"chat/init_db.yml", "chat/create_chat.yml", "chat/add_message.yml", "chat/add_attachment.yml"})
    @ExpectedDataSet("chat/remove_user.yml")
    @ExportDataSet(format = DataSetFormat.YML, outputName = "target/exported/testing.yml")
    public void shouldRemoveUserWithAllOfHisChatsAndMessagesWithAttachments() {
        User user = getExistingUser();
        userRepository.delete(user);
        userRepository.flush();
    }


    private User getExistingUser() {
        return userRepository.findByUsername("test2").orElseThrow(EntityNotFoundException::new);
    }

    private Advertisement getExistingAdvertisement() {
        return advertisementRepository.findAll().get(0);
    }
}