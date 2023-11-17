package space.obminyashka.items_exchange.rest.response.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseMessagesHandler {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ValidationMessage {
        public static final String INVALID_NOT_POSITIVE_ID = "invalid.not-positive.id";
        public static final String INVALID_NOT_EMPTY = "invalid.not-empty";
        public static final String INVALID_NOT_NULL = "invalid.not-null";
        public static final String INVALID_SIZE = "invalid.size";
        public static final String INVALID_MAX_SIZE = "invalid.max-size";
        public static final String INVALID_EMAIL = "invalid.email";
        public static final String DUPLICATE_EMAIL = "email.duplicate";
        public static final String USERNAME_EMAIL_DUPLICATE = "username-email.duplicate";
        public static final String INVALID_USERNAME = "invalid.username";
        public static final String INVALID_USERNAME_SIZE = "invalid.username.size";
        public static final String INVALID_USERNAME_PASSWORD = "invalid.username-or-password";
        public static final String INVALID_PASSWORD = "invalid.password";
        public static final String INVALID_PASSWORD_SIZE = "invalid.password.size";
        public static final String DIFFERENT_PASSWORDS = "different.passwords";
        public static final String SAME_PASSWORDS = "same.passwords";
        public static final String INVALID_PAST_PRESENT_DATE = "invalid.past-or-present.date";
        public static final String INVALID_OAUTH2_LOGIN = "invalid.oauth2.login";
        public static final String INVALID_PHONE_NUMBER = "invalid.phone.number";
        public static final String INVALID_PHONES_AMOUNT = "invalid.phones-amount";
        public static final String INVALID_FIRST_LAST_NAME = "invalid.first-or-last.name";
        public static final String INVALID_CHILD_AGE = "invalid.child.age";
        public static final String CATEGORY_NOT_DELETABLE = "category.not-deletable";
        public static final String INVALID_NEW_CATEGORY_DTO = "invalid.new-category-dto";
        public static final String INVALID_UPDATED_CATEGORY_DTO = "invalid.updated-category.dto";
        public static final String SUBCATEGORY_NOT_DELETABLE = "subcategory.not-deletable";
        public static final String INVALID_CATEGORY_ID = "invalid.category.id";
        public static final String INVALID_SUBCATEGORY_ID = "invalid.subcategory.id";
        public static final String INVALID_SIZE_COMBINATION = "invalid.size.combination";
        public static final String INVALID_CATEGORY_SIZES_ID = "invalid.category.sizes.id";
        public static final String INVALID_CATEGORY_SUBCATEGORY_COMBINATION = "invalid.category.subcategory.combination";
        public static final String INVALID_ENUM_VALUE = "invalid.enum.value";
        public static final String INVALID_LOCATION_ID = "invalid.location.id";
        public static final String INVALID_TOKEN = "invalid.token";
        public static final String INVALID_REFRESH_TOKEN = "refresh.token.invalid";
        public static final String USER_NOT_REGISTERED = "user.not-registered";
        public static final String USER_NOT_OWNER = "user.not-owner";
        public static final String USER_CREATED = "user.created";
        public static final String EMPTY_USERNAME = "empty.username";
        public static final String EMPTY_PASSWORD = "empty.password";
        public static final String EMPTY_CONFIRM_PASS = "empty.confirm.password";
        public static final String EMPTY_EMAIL = "empty.email";
        public static final String BLANK_TOPIC = "blank.topic";
        public static final String BLANK_DESCRIPTION = "blank.description";
        public static final String BLANK_WISHES_TO_EXCHANGE = "blank.wishes.to.exchange";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ExceptionMessage {
        public static final String USER_NOT_FOUND = "exception.user.not-found";
        public static final String ILLEGAL_ID = "exception.illegal.id";
        public static final String ILLEGAL_OPERATION = "exception.illegal.operation";
        public static final String CHILDREN_AMOUNT = "exception.children-amount";
        public static final String EXCEED_IMAGES_NUMBER = "exception.exceed.images.number";
        public static final String ADVERTISEMENT_IMAGE_ID_NOT_FOUND = "exception.advertisement-image.id.not-found";
        public static final String IMAGE_NOT_EXISTED_ID = "exception.image.not-existed-id";
        public static final String ADVERTISEMENT_NOT_EXISTED_ID = "exception.advertisement.not-existed-id";
        public static final String FAVORITE_ADVERTISEMENT_NOT_FOUND = "exception.favorite.advertisement.not-found";
        public static final String EMAIL_SENDING = "exception.email.sending";
        public static final String EMAIL_OLD = "exception.email.old";
        public static final String LOCATION_ALREADY_EXIST = "exception.location.exist";
        public static final String EMAIL_NOT_FOUND_OR_EXPIRED = "exception.email.validation-code";
        public static final String NOT_IMPLEMENTED = "exception.not-implemented";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PositiveMessage {
        public static final String CHANGED_USER_PASSWORD = "changed.user.password";
        public static final String CHANGED_USER_EMAIL = "changed.user.email";
        public static final String CHANGED_USER_INFO = "changed.user.info";
        public static final String DELETE_ACCOUNT = "account.self.delete.request";
        public static final String ACCOUNT_ACTIVE_AGAIN = "account.made.active.again";
        public static final String EMAIL_CONFIRMED = "email.confirmed";
        public static final String RESET_PASSWORD = "reset.password";
    }
}