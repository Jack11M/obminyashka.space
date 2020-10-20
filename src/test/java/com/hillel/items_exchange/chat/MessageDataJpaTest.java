package com.hillel.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dao.ChatRepository;
import com.hillel.items_exchange.dao.MessageRepository;
import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.Chat;
import com.hillel.items_exchange.model.Message;
import com.hillel.items_exchange.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@DBRider
public class MessageDataJpaTest {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    MessageRepository messageRepository;

    @Test
    @DataSet("chat/create_chat.yml")
    @ExpectedDataSet("chat/add_message.yml")
    public void addMessageToChat_Success() {
       final User validUser = getValidUser();
        final Chat chat = validUser.getChats().get(0);
        messageRepository.save(createMessage("message text", chat));
    }

    @Test
    @DataSet("chat/create_chat.yml")
    @ExpectedDataSet("chat/remove_user.yml")
    public void removeUser_shouldRemoveUserAndUserChats() {
        final User validUser = getValidUser();
        userRepository.delete(validUser);
        userRepository.flush();
    }

    private User getValidUser() {
        return userRepository.findByUsername("user").orElseThrow(EntityNotFoundException::new);
    }

    private Advertisement getValidAdvertisement() {
        return advertisementRepository.findAll().get(0);
    }

    private Message createMessage(String text, Chat chat) {
        Message message = new Message();
        message.setText(text);
        message.setChat(chat);
        //        message.setUser(user);
        return message;
    }

}