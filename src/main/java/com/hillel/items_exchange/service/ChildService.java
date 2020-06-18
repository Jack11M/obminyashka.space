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

    public void updateAll(List<Child> newChildrenList, User user) {
        newChildrenList.forEach(child -> {
            if (child.getId() == 0) {
               user.addChild(child);
            } else {
                Child existedChild = user.getChildren().stream()
                        .filter(c -> c.getId() == child.getId())
                        .findAny()
                        .orElseThrow(() -> new UnprocessableEntityException(
                                messageSourceUtil.getExceptionMessageSource("exception.invalid.dto")));
                BeanUtils.copyProperties(child, existedChild, "id", "user");
            }
        });
        user.getChildren().removeIf(child ->
                newChildrenList.stream().map(Child::getId).noneMatch(id -> id == child.getId()));
    }
}