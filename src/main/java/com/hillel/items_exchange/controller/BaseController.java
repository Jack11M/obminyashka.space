package com.hillel.items_exchange.controller;

import com.hillel.items_exchange.exception.IllegalOperationException;
import com.hillel.items_exchange.model.User;
import com.hillel.items_exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import static com.hillel.items_exchange.model.enums.Status.DELETED;
import static com.hillel.items_exchange.util.MessageSourceUtil.getExceptionParametrizedMessageSource;
import static com.hillel.items_exchange.util.MessageSourceUtil.getMessageSource;

public abstract class BaseController {

    @Autowired
    private UserService userService;

    void checkIfUserStatusIsDeleted(User user) throws IllegalOperationException {
        if (user.getStatus().equals(DELETED)) {
            throw new IllegalOperationException(getMessageSource("exception.illegal.operation")
                    .concat(". ")
                    .concat(getExceptionParametrizedMessageSource("account.deleted.first",
                            userService.getDaysBeforeDeletion(user))));
        }
    }
}
