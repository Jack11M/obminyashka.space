package space.obminyashka.items_exchange.rest.regexp;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static space.obminyashka.items_exchange.rest.regexp.PatternHandler.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternHandlerTest {

    @ParameterizedTest
    @MethodSource("createCorrectPasswords")
    void testPasswordRegexp_whenPasswordsCorrect(String correctPassword) {
        assertTrue(correctPassword.matches(PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("createWrongPasswords")
    void testPasswordRegexp_whenPasswordsWrong(String wrongPassword) {
        assertFalse(wrongPassword.matches(PASSWORD));
    }

    @ParameterizedTest
    @MethodSource("createCorrectEmails")
    void testEmailRegexp_whenEmailsCorrect(String correctEmail) {
        assertTrue(correctEmail.matches(EMAIL));
    }

    @ParameterizedTest
    @MethodSource("createWrongEmails")
    void testEmailRegexp_whenEmailsWrong(String wrongEmail) {
        assertFalse(wrongEmail.matches(EMAIL));
    }

    @ParameterizedTest
    @MethodSource("createCorrectPhoneNumbers")
    void testPhoneNumberRegexp_whenPhoneNumberIsCorrect(String correctPhoneNumber) {
        assertTrue(correctPhoneNumber.matches(PHONE_NUMBER));
    }

    @ParameterizedTest
    @MethodSource("createWrongPhoneNumbers")
    void testPhoneNumberRegexp_whenPhoneNumberIsWrong(String wrongPhoneNumber) {
        assertFalse(wrongPhoneNumber.matches(PHONE_NUMBER));
    }

    @ParameterizedTest
    @MethodSource("createCorrectUserNames")
    void testUsernameRegexp_whenUsernameIsCorrect(String correctUserName) {
        assertTrue(correctUserName.matches(USERNAME));
    }

    @ParameterizedTest
    @MethodSource("createWrongUserNames")
    void testUsernameRegexp_whenUsernameIsWrong(String wrongName) {
        assertFalse(wrongName.matches(USERNAME));
    }

    @ParameterizedTest
    @MethodSource("createCorrectWordsEmptyOrMin2Max50")
    void testWordEmptyOrMin2Max50_whenWordIsCorrect(String correctWord) {
        assertTrue(correctWord.matches(WORD_EMPTY_OR_MIN_2_MAX_50));
    }

    @ParameterizedTest
    @MethodSource("createWrongWordsEmptyOrMin2Max50")
    void testWordEmptyOrMin2Max50_whenWordIsWrong(String wrongWord) {
        assertFalse(wrongWord.matches(WORD_EMPTY_OR_MIN_2_MAX_50));
    }

    private static List<String> createCorrectPasswords() {

        return List.of(
                "wW5",
                "wW5".concat("`~!@#$%^&*()_+}{[]"),
                "wW5".concat("123``*()"),
                "wW5".concat("1!2@3#"),
                "wW5".concat("WWWWWWWWWWWqqqqqqqqqqqq~!@#$%^&*()_+}{[]<>?/.,|\\'\"")
        );
    }

    private static List<String> createWrongPasswords() {

        return List.of(
                "",
                " ",
                "W",
                "5",
                "w5",
                "5W wW",
                "wW5 ",
                "5W~~##@@",
                "5WwФЁёЪ",
                "5wW~!@#$Ъ"
        );
    }

    private static List<String> createCorrectEmails() {
        return List.of(
                "username@domain.com",
                "user.name@domain.com",
                "user-name@domain.com",
                "username@domain.co.in",
                "user_name@domain.com"
        );
    }

    private static List<String> createWrongEmails() {
        return List.of(
                "username@domain",
                "username.@domain.com",
                ".user.name@domain.com",
                "user-name@domain.com.",
                "username@.com"
        );
    }

    private static List<String> createCorrectPhoneNumbers() {

        return List.of(
                "+380994785144",
                "+133456789013");
    }

    private static List<String> createWrongPhoneNumbers() {

        return List.of(
                "+1(234)567-89-10)",
                "+1-345-678-90-12",
                "++12-345-678-90-12",
                "-13.345.678.90.13",
                "345.678.90.13",
                "+1 2-345-678-90-12",
                "13.34 5.678.90.13",
                "+12-345-678-9 0-12",
                "13~345.678.90.13",
                "+12-)345(-678-90-12",
                "13.3W5.678.90.13",
                "+12-345-(678)-90-12",
                "13.345.6#8.90.13",
                "+12W345-678-90-12",
                "13Ё345.678.90.13"
        );
    }

    private static List<String> createCorrectUserNames() {

        return List.of(
                "pushkin",
                "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "АлександрФилипповичМакедонский",
                "7",
                "WЪQЬRЫL",
                "Ё",
                "Є"
        );
    }

    private static List<String> createWrongUserNames() {

        return List.of(
                "",
                " ",
                "www bbb",
                " www",
                "www ",
                "~~!@#$%^&&*())",
                "user@gmail.com"
        );
    }

    private static List<String> createCorrectWordsEmptyOrMin2Max50() {

        return List.of(
                "",
                "wW",
                "75",
                "--",
                "''",
                "``",
                "__",
                "ёЁ",
                "єЄ",
                "ThisWordHasLength50SymbolsThisWordHasLength50Symbo"
        );
    }

    private static List<String> createWrongWordsEmptyOrMin2Max50() {

        return List.of(
                "w",
                "W",
                "1",
                " ",
                "  ",
                "  +correct",
                "correct+~!@",
                "correct+   ",
                "correct+   +correct",
                "ThisWordHasLengthMoreThan50SymbolsThisWordHasLength"
        );
    }
}
