package space.obminyashka.items_exchange.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseMessagesHandler {

    @UtilityClass
    public class ValidationMessage {
        public final String INVALID_NOT_POSITIVE_ID = "invalid.not-positive.id";
        public final String INVALID_NOT_EMPTY = "invalid.not-empty";
        public final String INVALID_NOT_NULL = "invalid.not-null";
        public final String INVALID_SIZE = "invalid.size";
        public final String INVALID_MAX_SIZE = "invalid.max-size";
        public final String INVALID_EMAIL = "invalid.email";
        public final String INVALID_CONFIRM_EMAIL = "invalid.confirm.email";
        public final String DUPLICATE_EMAIL = "email.duplicate";
        public final String USERNAME_EMAIL_DUPLICATE = "username-email.duplicate";
        public final String INVALID_USERNAME = "invalid.username";
        public final String INVALID_USERNAME_SIZE = "invalid.username.size";
        public final String INVALID_USERNAME_PASSWORD = "invalid.username-or-password";
        public final String INVALID_PASSWORD = "invalid.password";
        public final String INVALID_PASSWORD_SIZE = "invalid.password.size";
        public final String INCORRECT_PASSWORD = "incorrect.password";
        public final String DIFFERENT_PASSWORDS = "different.passwords";
        public final String SAME_PASSWORDS = "same.passwords";
        public final String INVALID_PAST_PRESENT_DATE = "invalid.past-or-present.date";
        public final String INVALID_OAUTH2_LOGIN = "invalid.oauth2.login";
        public final String INVALID_PHONE_NUMBER = "invalid.phone.number";
        public final String INVALID_PHONES_AMOUNT = "invalid.phones-amount";
        public final String INVALID_FIRST_LAST_NAME = "invalid.first-or-last.name";
        public final String INVALID_CHILD_AGE = "invalid.child.age";
        public final String CATEGORY_NOT_DELETABLE = "category.not-deletable";
        public final String INVALID_NEW_CATEGORY_DTO = "invalid.new-category-dto";
        public final String INVALID_UPDATED_CATEGORY_DTO = "invalid.updated-category.dto";
        public final String SUBCATEGORY_NOT_DELETABLE = "subcategory.not-deletable";
        public final String INVALID_SUBCATEGORY_ID = "invalid.subcategory.id";
        public final String INVALID_LOCATION_ID = "invalid.location.id";
        public final String INVALID_TOKEN = "invalid.token";
        public final String INVALID_REFRESH_TOKEN = "refresh.token.invalid";
        public final String USER_NOT_REGISTERED = "user.not-registered";
        public final String USER_NOT_OWNER = "user.not-owner";
        public final String USER_CREATED = "user.created";
        public final String EMPTY_USERNAME = "empty.username";
        public final String EMPTY_PASSWORD = "empty.password";
        public final String EMPTY_CONFIRM_PASS = "empty.confirm.password";
        public final String EMPTY_EMAIL = "empty.email";
    }

    @UtilityClass
    public class ExceptionMessage {
        public final String USER_NOT_FOUND = "exception.user.not-found";
        public final String ILLEGAL_ID = "exception.illegal.id";
        public final String ILLEGAL_OPERATION = "exception.illegal.operation";
        public final String CHILDREN_AMOUNT = "exception.children-amount";
        public final String EXCEED_IMAGES_NUMBER = "exception.exceed.images.number";
        public final String ADVERTISEMENT_IMAGE_ID_NOT_FOUND = "exception.advertisement-image.id.not-found";
        public final String IMAGE_NOT_EXISTED_ID = "exception.image.not-existed-id";
        public final String EMAIL_REGISTRATION = "exception.emailing.registration";
        public final String EMAIL_OLD = "exception.email.old";
        public final String LOCATION_ALREADY_EXIST = "exception.location.exist";
    }

    @UtilityClass
    public class PositiveMessage {
        public final String CHANGED_USER_PASSWORD = "changed.user.password";
        public final String CHANGED_USER_EMAIL = "changed.user.email";
        public final String CHANGED_USER_INFO = "changed.user.info";
        public final String DELETE_ACCOUNT = "account.self.delete.request";
        public final String ACCOUNT_ACTIVE_AGAIN = "account.made.active.again";
        public final String EMAIL_REGISTRATION_HEADER = "registration.header";
        public final String EMAIL_REGISTRATION_BODY = "registration.body";
    }
}