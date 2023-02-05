package space.obminyashka.items_exchange.util;

import space.obminyashka.items_exchange.dto.*;

import java.util.HashSet;
import java.util.Set;

public class UserDtoCreatingUtil {

    public static final Set<PhoneDto> NEW_PHONES = Set.of(new PhoneDto("381234567890", true));
    public static final String NEW_VALID_EMAIL = "new.admin@gmail.com";
    public static final String NEW_INVALID_DUPLICATE_EMAIL = "test@test.com";
    public static final String INVALID_EMAIL = "test22@gmail";
    public static final String OLD_USER_VALID_EMAIL = "user@gmail.com";
    public static final String OLD_ADMIN_VALID_EMAIL = "admin@gmail.com";
    public static final String NEW_VALID_NAME_WITH_APOSTROPHE = "Мар'яна";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "Квітка-Основ'яненко";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";
    public static final String CORRECT_OLD_PASSWORD = "@kuIOIY*h986";
    public static final String WRONG_OLD_PASSWORD = "123456wWWW";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String WRONG_NEW_PASSWORD_CONFIRMATION = "123456qQ";
    private static final int MAX_AMOUNT_OF_PHONES = 3;
    public static final Set<PhoneDto> NEW_INVALID_PHONES = createWithInvalidSizeListOfPhones();

    public static UserUpdateDto createUserUpdateDto() {
        return new UserUpdateDto(NEW_VALID_NAME_WITH_APOSTROPHE, NEW_VALID_NAME_WITH_HYPHEN_MINUS, NEW_PHONES );
    }

    public static UserUpdateDto createUserUpdateDtoWithInvalidAmountOfPhones() {
        return new UserUpdateDto(NEW_VALID_NAME_WITH_APOSTROPHE, NEW_VALID_NAME_WITH_HYPHEN_MINUS, NEW_INVALID_PHONES );
    }

    public static UserUpdateDto createUserUpdateDtoWithInvalidFirstAndLastName() {
        return new UserUpdateDto(NEW_INVALID_SHORT_NAME, NEW_INVALID_TWO_WORDS_NAME, NEW_PHONES );
    }

    private static Set<PhoneDto> createWithInvalidSizeListOfPhones() {
        Set<PhoneDto> phoneDto = new HashSet<>();
        for (int i = 0; i <= MAX_AMOUNT_OF_PHONES; i++) {
            phoneDto.add(new PhoneDto("38123456789" + i, true));
        }
        return phoneDto;
    }
}
