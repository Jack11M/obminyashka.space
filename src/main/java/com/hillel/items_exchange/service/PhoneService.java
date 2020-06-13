package com.hillel.items_exchange.service;

import com.hillel.items_exchange.exception.UnprocessableEntityException;
import com.hillel.items_exchange.model.Phone;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.MessageSourceUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PhoneService {
    private final MessageSourceUtil messageSourceUtil;

    public void updateAll(List<Phone> updatedPhones, User currentUser) {
        updatedPhones.forEach(phone -> {
            if (phone.getId() == 0) {
                phone.setUser(currentUser);
                currentUser.getPhones().add(phone);
            } else {
                Phone existedPhone = currentUser.getPhones().stream()
                        .filter(p -> p.getId() == phone.getId())
                        .findAny()
                        .orElseThrow(() -> new UnprocessableEntityException(
                                messageSourceUtil.getExceptionMessageSource("exception.invalid.dto")));
                BeanUtils.copyProperties(phone, existedPhone, "id", "user");
            }
        });
        currentUser.getPhones().removeIf(phone ->
                updatedPhones.stream().map(Phone::getId).noneMatch(id -> id == phone.getId()));
    }
}