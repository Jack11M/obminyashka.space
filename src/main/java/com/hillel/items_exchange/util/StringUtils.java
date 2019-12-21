package com.hillel.items_exchange.util;

public class StringUtils {

    public static final String NO_CATEGORIES = "No categories";
    public static final String NO_CATEGORY_BY_ID = "There is no category by id: ";
    private static final String CATEGORY_CAN_NOT = "This category can not be ";
    public static final String CAN_NOT_BE_CREATED = CATEGORY_CAN_NOT + "created: ";
    public static final String CAN_NOT_BE_UPDATED = CATEGORY_CAN_NOT + "updated: ";
    public static final String CAN_NOT_BE_DELETED = CATEGORY_CAN_NOT + "deleted by this id: ";
    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String MUST_HAVE_ID_ZERO = "The new category has to have id == 0";
    public static final String ID_ZERO_OF_ALL_SUBCATEGORIES = MUST_HAVE_ID_ZERO + " of the all subcategories";
    public static final String CATEGORY_MUST_BE_DISTINCT = "The new category must not have a name like the existing category: ";
    public static final String CATEGORY_MUST_EXIST_BY_ID = "The category has to have id greater than 0 and exist by id";
    public static final String SUBCATEGORIES_MUST_EXIST_BY_ID_OR_ZERO = "The updated subcategories have to exist by id or be 0";
    public static final String NO_SUBCATEGORIES_BY_CATEGORY_ID = "There are no subcategories in the category by this id: ";
    public static final String SUBCATEGORY_CAN_NOT_BE_DELETED = "The subcategory can not be deleted by this id: ";
    private static final String DELETED_SUBCATEGORY = "The deleted subcategory ";
    public static final String MUST_NOT_HAVE_PRODUCTS = DELETED_SUBCATEGORY + "must not have products";
    public static final String MUST_EXIST_BY_ID = DELETED_SUBCATEGORY + "have to exist by id";
}
