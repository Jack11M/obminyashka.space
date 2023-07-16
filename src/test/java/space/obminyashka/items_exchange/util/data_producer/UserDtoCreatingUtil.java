package space.obminyashka.items_exchange.util.data_producer;

import space.obminyashka.items_exchange.rest.dto.PhoneDto;
import space.obminyashka.items_exchange.rest.request.MyUserInfoUpdateRequest;

import java.util.HashSet;
import java.util.Set;

public class UserDtoCreatingUtil {

    public static final Set<PhoneDto> NEW_PHONES = Set.of(new PhoneDto("+381234567890", true));
    public static final String NEW_VALID_EMAIL = "new.admin@gmail.com";
    public static final String INVALID_EMAIL_WITHOUT_POINT = "username@domain";
    public static final String INVALID_EMAIL_WITHOUT_DOMAIN_NAME = "username@.com";
    public static final String OLD_ADMIN_VALID_EMAIL = "admin@gmail.com";
    public static final String NEW_VALID_NAME_WITH_APOSTROPHE = "Мар'яна";
    public static final String NEW_VALID_NAME_WITH_HYPHEN_MINUS = "Квітка-Основ'яненко";
    public static final String NEW_INVALID_SHORT_NAME = "n";
    public static final String NEW_INVALID_TWO_WORDS_NAME = "new Name";
    public static final String CORRECT_OLD_PASSWORD = "@kuIOIY*h986";
    public static final String NEW_PASSWORD = "123456wW";
    public static final String WRONG_NEW_PASSWORD_CONFIRMATION = "123456qQ";
    private static final int MAX_AMOUNT_OF_PHONES = 3;
    public static final Set<PhoneDto> NEW_INVALID_PHONES = createWithInvalidSizeListOfPhones();
    public static final Set<PhoneDto> NEW_INVALID_PHONE = Set.of(new PhoneDto(null, true));

    public static MyUserInfoUpdateRequest createUserUpdateDto() {
        return new MyUserInfoUpdateRequest(NEW_VALID_NAME_WITH_APOSTROPHE, NEW_VALID_NAME_WITH_HYPHEN_MINUS, NEW_PHONES );
    }

    public static MyUserInfoUpdateRequest createUserUpdateDtoWithInvalidAmountOfPhones() {
        return new MyUserInfoUpdateRequest(NEW_VALID_NAME_WITH_APOSTROPHE, NEW_VALID_NAME_WITH_HYPHEN_MINUS, NEW_INVALID_PHONES );
    }

    public static MyUserInfoUpdateRequest createUserUpdateDtoWithInvalidFirstAndLastName() {
        return new MyUserInfoUpdateRequest(NEW_INVALID_SHORT_NAME, NEW_INVALID_TWO_WORDS_NAME, NEW_PHONES );
    }

    public static MyUserInfoUpdateRequest createUserUpdateDtoWithInvalidNullPhone() {
        return new MyUserInfoUpdateRequest(NEW_VALID_NAME_WITH_APOSTROPHE, NEW_VALID_NAME_WITH_HYPHEN_MINUS, NEW_INVALID_PHONE );
    }

    private static Set<PhoneDto> createWithInvalidSizeListOfPhones() {
        Set<PhoneDto> phoneDto = new HashSet<>();
        for (int i = 0; i <= MAX_AMOUNT_OF_PHONES; i++) {
            phoneDto.add(new PhoneDto("38123456789" + i, true));
        }
        return phoneDto;
    }
}
