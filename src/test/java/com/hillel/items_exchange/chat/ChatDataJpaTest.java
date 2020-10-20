package com.hillel.items_exchange.chat;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hillel.items_exchange.dao.AdvertisementRepository;
import com.hillel.items_exchange.dao.ChatRepository;
import com.hillel.items_exchange.dao.UserRepository;
import com.hillel.items_exchange.model.Advertisement;
import com.hillel.items_exchange.model.Chat;
import com.hillel.items_exchange.model.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@DBRider
public class ChatDataJpaTest {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdvertisementRepository advertisementRepository;

    @Test
    @DataSet("chat/init.yml")
    @ExpectedDataSet("chat/create_chat.yml")
    public void addChatToUser_Success() {
        Chat chat = createChat("test hash", getValidAdvertisement(), getValidUser());
        final User validUser = getValidUser();
        validUser.getChats().add(chat);
        userRepository.save(validUser);
        userRepository.flush();
    }

//    @Ignore
    @Test
    @DataSet("chat/init.yml")
    @ExportDataSet(format = DataSetFormat.YML, outputName = "target/exported/insert_chat_result.yml")
    public void testFindAllAndExport() {
        Chat chat = createChat("test hash", getValidAdvertisement(), getValidUser());
        final User validUser = getValidUser();
        validUser.getChats().add(chat);
        userRepository.save(validUser);
        userRepository.flush();
    }

    @Test
    @DataSet("chat/init.yml")
    public void addChatToUser_Success2() {
        Chat chat = createChat("test hash", getValidAdvertisement(), getValidUser());
        final User validUser = getValidUser();
        validUser.getChats().add(chat);
        userRepository.save(validUser);
        assertTrue(getValidUser().getChats().stream().anyMatch(c -> c.getHash().equals("test hash")));
        assertTrue(getValidAdvertisement().getChats().stream().anyMatch(c -> c.getHash().equals("test hash")));
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

    private Chat createChat(String hash, Advertisement advertisement, User user) {
        Chat chat = new Chat();
        chat.setHash(hash);
        chat.setAdvertisement(advertisement);
//        chat.setUsers(List.of(user));
        return chat;
    }

}