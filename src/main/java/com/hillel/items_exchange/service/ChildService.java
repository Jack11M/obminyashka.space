package com.hillel.items_exchange.service;

import com.hillel.items_exchange.exception.UnprocessableEntityException;
import com.hillel.items_exchange.model.Child;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.util.MessageSourceUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChildService {
    private final MessageSourceUtil messageSourceUtil;

    public void updateAll(List<Child> updatedChildren, User currentUser) {
        updatedChildren.forEach(child -> {
            if (child.getId() == 0) {
                child.setUser(currentUser);
                currentUser.getChildren().add(child);
            } else {
                Child existedChild = currentUser.getChildren().stream()
                        .filter(c -> c.getId() == child.getId())
                        .findAny()
                        .orElseThrow(() -> new UnprocessableEntityException(
                                messageSourceUtil.getExceptionMessageSource("exception.invalid.dto")));
                BeanUtils.copyProperties(child, existedChild, "id", "user");
            }
        });
        currentUser.getChildren().removeIf(child ->
                updatedChildren.stream().map(Child::getId).noneMatch(id -> id == child.getId()));
    }
}