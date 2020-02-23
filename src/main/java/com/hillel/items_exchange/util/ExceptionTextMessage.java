package com.hillel.items_exchange.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionTextMessage {

    public static final String NO_CATEGORY_BY_ID = "There is no category by id: ";
    public static final String MUST_HAVE_ID_ZERO = "New category or subcategory mustn't contain id except 0, " +
            "also category name mustn't have duplicates!";
    public static final String SUBCATEGORIES_MUST_EXIST_BY_ID_OR_ZERO = "The updated category and subcategories have " +
            "to exist by id, new subcategories have to be 0 and also category name mustn't have duplicates except current name!";
    public static final String CATEGORY_CAN_NOT_BE_DELETED = "The category can not be deleted by this id, " +
            "because it has to exist by id and it's subcategories mustn't have products! Given category id: ";
    public static final String SUBCATEGORY_CAN_NOT_BE_DELETED = "The subcategory can not be deleted by this id," +
            " because it has to exist by id and mustn't have products! Given subcategory id: ";
    public static final String SQL_EXCEPTION = "Exception during saving object to the database!\nError message:\n";
}
