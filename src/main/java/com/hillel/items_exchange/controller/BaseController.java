package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.UserService;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

import static com.hillel.items_exchange.model.enums.Status.DELETED;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionParametrizedMessageSource;

public abstract class BaseController {

    @Value("${number.of.days.to.keep.deleted.users}")
    private int numberOfDaysToKeepDeletedUsers;

    private UserService userService;

    void checkUserStatusDeleted(User user) throws IllegalOperationException {
        if (user.getStatus().equals(DELETED)) {
            throw new IllegalOperationException(getExceptionParametrizedMessageSource("account.deleted",
                    LocalDate.now().plusDays(numberOfDaysToKeepDeletedUsers), userService.getTimeWhenUserShouldBeDeleted()));
        }
    }
}
