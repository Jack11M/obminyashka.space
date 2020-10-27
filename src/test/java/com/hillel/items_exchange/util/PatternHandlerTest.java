package com.hillel.items_exchange.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.hillel.items_exchange.util.PatternHandler.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternHandlerTest {

    private static final String REGROUPED_PHONE_NUMBER_PATTERN =
            "^\\s*(?<country>\\+?\\d{2})([-. (]*)(?<area>\\d{3})([-. )]*)(\\d{3})([-. ]*)(\\d{2})([-. ]*)(\\d{2})\\s*$";
    public static final String REGROUPED_EMAIL_PATTERN = "^([\\w-+]+)(\\.[\\w]+)*(@)([\\w-]+)(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";
    public static final String REGROUPED_PASSWORD_PATTERN = "(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=\\S+$)([\\w\\p{Punct}]+)";

    @ParameterizedTest
    @MethodSource("createCorrectEmails")
    void testEmailRegexp_whenEmailIsCorrect(String correctEmail) {
        assertTrue(correctEmail.matches(EMAIL));
    }

    @ParameterizedTest
    @MethodSource("createWrongEmails")
    void testEmailRegexp_whenEmailIsWrong(String wrongEmail) {
        assertFalse(wrongEmail.matches(EMAIL));
    }

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

    private static List<String> createCorrectEmails() {

        return List.of(
                "pushkin@ukr.net",
                "pushkin.145@gmail.com",
                "pushkin@mail.ru",
                "pushkin.pushkin@mail.ru",
                "pushkin.pushkin@mail.mail.ru",
                "1@ukr.net",
                "1@111.ru");
    }

    private static List<String> createWrongEmails() {
        List<String> wrongEmails = new ArrayList<>(List.of(
                "pushkin@ukr.n",
                ".145@gmail.com",
                "pushkin@@mail.ru",
                "1@111.11",
                "pushkin.11",
                "pushkin.1u",
                "pushkin.u1",
                "1@111.1u",
                "1@111.1ru",
                "1@111.ru1",
                "1@ma~il.ru"));

        List<String> specialCharsAndRusUkrLetters = getSpecialCharacters("\\");
        specialCharsAndRusUkrLetters.removeAll(List.of("\\.", "\\_", "\\+", "\\-"));
        specialCharsAndRusUkrLetters.addAll(getRussianAndUkrLetters());
        specialCharsAndRusUkrLetters.forEach(
                specialChar -> createCorrectEmails().forEach(
                        email -> wrongEmails.add(email.replaceAll(
                                REGROUPED_EMAIL_PATTERN, "$1".concat(specialChar).concat("$2$3$4$5$6"))))
        );

        return wrongEmails;
    }

    private static List<String> createCorrectPasswords() {
        List<String> correctPasswords = new ArrayList<>();
        String minCorrectPassword = "aA1";
        correctPasswords.add(minCorrectPassword);

        getSpecialCharacters("\\").forEach(
                specialChar -> correctPasswords.add(minCorrectPassword.replaceAll(
                        REGROUPED_PASSWORD_PATTERN, specialChar.concat("$1")))
        );

        return correctPasswords;
    }

    private static List<String> createWrongPasswords() {
        List<String> wrongPasswords = new ArrayList<>(List.of(
                "",
                "a",
                "1",
                "A",
                "a1",
                "aA",
                "A1",
                "aa1",
                "a11",
                "AA1",
                " aA1",
                "aA1 ",
                " aA1 ",
                "a A1",
                "a`1",
                "A`1",
                "1#A",
                "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~"));

        getRussianAndUkrLetters().forEach(letter ->
                createCorrectPasswords().forEach(password -> wrongPasswords.add(
                        password.replaceAll(REGROUPED_PASSWORD_PATTERN, letter.concat("S1")))));

        return wrongPasswords;
    }

    private static List<String> getCorrectPhoneNumbers() {
        List<String> correctPhoneNumbers = new ArrayList<>();

        String correctNumber = "+12-345-678-90-12";
        String correctNumber1 = "13.345.678.90.13";

        correctPhoneNumbers.add(correctNumber);
        correctPhoneNumbers.add(correctNumber1);

        return correctPhoneNumbers;
    }

    private static List<String> createCorrectPhoneNumbers() {
        List<String> correctPhoneNumbers = getCorrectPhoneNumbers();

        List<String> tempList = new ArrayList<>();
        for (String str : correctPhoneNumbers) {
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "   $1-$2-$3-$4-$5-$6-$7-$8-$9   "));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "   $1.$2.$3.$4.$5.$6.$7.$8.$9   "));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$3$5$7$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1     $3     $5     $7     $9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2($3)$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1($3)$5$7$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1(.- $3 -.)$5 -.- $7 -.- $9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2(   $3   )$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2((($3)))$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2(((((($3.-.-.)$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2(---$3...)$4$5$6$7$8$9"));
        }
        correctPhoneNumbers.addAll(tempList);

        return correctPhoneNumbers;
    }

    private static List<String> createWrongPhoneNumbers() {
        List<String> wrongPhoneNumbers = getCorrectPhoneNumbers();
        List<String> tempList = new ArrayList<>();
        for (String str : wrongPhoneNumbers) {
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, " $2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "+3$2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "+ 3$2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "+$2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "3$2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$212$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2 12$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2 12 $4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2+12$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2(12)$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2( 12)$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2(+12)$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2)$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2$3($4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2)$3($4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2$3$4($5)$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2$3$4$5($6)$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "+ 38$2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "++38$2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "+3 8$2$3$4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2 0 50 $4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2 05 0 $4$5$6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2$3$4 67 8 $6$7$8$9"));
            tempList.add(str.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$1$2$3$4 6 7 8 $6$7$8$9"));
        }

        List<String> specialChars = getSpecialCharacters("\\");
        specialChars.removeAll(List.of("\\.", "\\-"));

        specialChars.forEach(
                specialChar -> wrongPhoneNumbers.forEach(
                        number -> {
                            tempList.add(
                                    number.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN,
                                            "$1$2$3$4$5".concat(specialChar).concat("$7$8$9")));
                            String twoDigits = number.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$7");
                            tempList.add(
                                    number.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN,
                                            "$1$2$3$4$5$6".concat("\\".concat(twoDigits.replaceAll("^\\d", specialChar))).concat("$8$9")));
                        }));

        wrongPhoneNumbers.clear();
        wrongPhoneNumbers.addAll(tempList);

        return wrongPhoneNumbers;
    }

    private static List<String> createCorrectUserNames() {
        List<String> correctNames = new ArrayList<>(List.of(
                "pushkin",
                "ThisNameHasLengthMoreThan100SymbolsThisNameHasLengthMoreThan100SymbolsThisNameHasLengthMoreThan100Symbols",
                "1"));
        List<String> specialChars = getSpecialCharacters("");
        specialChars.addAll(getRussianAndUkrLetters());

        correctNames.addAll(specialChars);

        return correctNames;
    }

    private static List<String> createWrongUserNames() {

        return List.of(
                "",
                " ",
                "aaa bbb",
                " aaa",
                "aaa ");
    }

    private static List<String> createCorrectWordsEmptyOrMin2Max50() {
        List<String> correctWords = new ArrayList<>(List.of(
                "aZ",
                "99",
                "Za0",
                "wW1-",
                "wW2-w",
                "wW3'",
                "wW4'w",
                "wW5`",
                "wW6`w",
                "wW7_",
                "wW8_w"
        ));

        List<String> tempList = new ArrayList<>();
        correctWords.forEach(word -> getRussianAndUkrLetters().forEach(
                letter -> tempList.add(word.concat(letter))
        ));

        correctWords.addAll(tempList);
        correctWords.addAll(List.of(
                "",
                "LengthIs50LengthIs50LengthIs50LengthIs50LengthIs50"));

        return correctWords;
    }

    private static List<String> createWrongWordsEmptyOrMin2Max50() {
        String correctWord = "a1A";

        List<String> wrongWords = new ArrayList<>(List.of(
                "w",
                "W",
                "1",
                " ",
                " 1",
                " W",
                " w",
                "1 ",
                "1 ",
                "W ",
                "w ",
                "ThisWordHasLengthMoreThan50CharactersItsLengthIs51S"));

        List<String> specialChars = getSpecialCharacters("");
        specialChars.removeAll(List.of("_", "-", "`", "'"));

        specialChars.forEach(specialChar -> wrongWords.add(correctWord.concat(specialChar)));

        return wrongWords;
    }

    private static List<String> getSpecialCharacters(String escapedSymbol) {
        List<String> specialCharacters = new ArrayList<>();

        IntStream.range(33, 48).forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));
        IntStream.range(58, 65).forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));
        IntStream.range(91, 97).forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));
        IntStream.range(123, 127).forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));

        return specialCharacters;
    }

    private static List<String> getRussianAndUkrLetters() {
        List<String> russianAndUkrLetters = new ArrayList<>();

        russianAndUkrLetters.add(String.valueOf((char) 1025));
        russianAndUkrLetters.add(String.valueOf((char) 1028));
        IntStream.range(1030, 1032).forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));
        IntStream.range(1040, 1104).forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));
        russianAndUkrLetters.add(String.valueOf((char) 1105));
        russianAndUkrLetters.add(String.valueOf((char) 1108));
        IntStream.range(1110, 1112).forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));
        IntStream.range(1168, 1170).forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));

        return russianAndUkrLetters;
    }
}