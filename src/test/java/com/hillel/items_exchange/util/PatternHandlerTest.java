package com.hillel.items_exchange.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.hillel.items_exchange.util.PatternHandler.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternHandlerTest {

    private static final String REGROUPED_PHONE_NUMBER_PATTERN =
            "^\\s*(?<country>\\+?\\d{2})([-. (]*)(?<area>\\d{3})([-. )]*)(\\d{3})([-. ]*)(\\d{2})([-. ]*)(\\d{2})\\s*$";
    public static final String REGROUPED_EMAIL_PATTERN =
            "^([\\w-+]+)(\\.[\\w]+)*(@)([\\w-]+)(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";
    public static final String REGROUPED_PASSWORD_PATTERN =
            "(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=\\S+$)([\\w\\p{Punct}]+)";
    public static final String ENGLISH_LETTERS_AND_NUMBERS =
            "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

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
        String minCorrectPassword = "aA1";
        List<String> wrongPasswords = new ArrayList<>(List.of(
                "",
                " ",
                "w",
                "1",
                "W",
                "`"
        ));

        List<String> tempList = new ArrayList<>();
        IntStream.range(0, wrongPasswords.size()).forEach(i -> {
            for (String wrongPassword : wrongPasswords) {
                tempList.add(wrongPasswords.get(i).concat(wrongPassword));
                tempList.add(wrongPasswords.get(i).concat(wrongPassword).concat(wrongPasswords.get(i)));
            }
        });

        wrongPasswords.addAll(tempList);

        wrongPasswords.addAll(List.of(
                minCorrectPassword.concat(" "),
                " ".concat(minCorrectPassword),
                minCorrectPassword.concat(" A"),
                "A ".concat(minCorrectPassword)
        ));

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
        List<String> replacementList = List.of(
                "   $1-$2-$3-$4-$5-$6-$7-$8-$9   ",
                "   $1.$2.$3.$4.$5.$6.$7.$8.$9   ",
                "$1$3$5$7$9",
                "$1     $3     $5     $7     $9",
                "$1$2($3)$4$5$6$7$8$9",
                "$1($3)$5$7$9",
                "$1(.- $3 -.)$5 -.- $7 -.- $9",
                "$1$2(   $3   )$4$5$6$7$8$9",
                "$1$2((($3)))$4$5$6$7$8$9",
                "$1$2(((((($3.-.-.)$4$5$6$7$8$9",
                "$1$2(---$3...)$4$5$6$7$8$9");

        List<String> tempList = new ArrayList<>();
        for (String correctNumber : correctPhoneNumbers) {
            addReplacedPhoneNumbers(replacementList, tempList, correctNumber);
        }
        correctPhoneNumbers.addAll(tempList);

        return correctPhoneNumbers;
    }

    private static List<String> createWrongPhoneNumbers() {
        List<String> wrongPhoneNumbers = getCorrectPhoneNumbers();
        List<String> replacementList = List.of(
                " $2$3$4$5$6$7$8$9",
                "+3$2$3$4$5$6$7$8$9",
                "+ 3$2$3$4$5$6$7$8$9",
                "+$2$3$4$5$6$7$8$9",
                "3$2$3$4$5$6$7$8$9",
                "$1$212$4$5$6$7$8$9",
                "$1$2 12$4$5$6$7$8$9",
                "$1$2 12 $4$5$6$7$8$9",
                "$1$2+12$4$5$6$7$8$9",
                "$1$2(12)$4$5$6$7$8$9",
                "$1$2( 12)$4$5$6$7$8$9",
                "$1$2(+12)$4$5$6$7$8$9",
                "$1$2)$3$4$5$6$7$8$9",
                "$1$2$3($4$5$6$7$8$9",
                "$1$2)$3($4$5$6$7$8$9",
                "$1$2$3$4($5)$6$7$8$9",
                "$1$2$3$4$5($6)$7$8$9",
                "+ 38$2$3$4$5$6$7$8$9",
                "++38$2$3$4$5$6$7$8$9",
                "+3 8$2$3$4$5$6$7$8$9",
                "$1$2 0 50 $4$5$6$7$8$9",
                "$1$2 05 0 $4$5$6$7$8$9",
                "$1$2$3$4 67 8 $6$7$8$9",
                "$1$2$3$4 6 7 8 $6$7$8$9");

        List<String> tempList = new ArrayList<>();
        for (String wrongNumber : wrongPhoneNumbers) {
            addReplacedPhoneNumbers(replacementList, tempList, wrongNumber);
        }

        List<String> specialChars = getSpecialCharacters("\\");
        specialChars.removeAll(List.of("\\.", "\\-"));

        specialChars.forEach(
                specialChar -> wrongPhoneNumbers.forEach(
                        number -> {
                            tempList.add(number.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN,
                                    "$1$2$3$4$5".concat(specialChar).concat("$7$8$9")));

                            String twoDigits = number
                                    .replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$7");
                            tempList.add(number.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN,
                                    "$1$2$3$4$5$6".concat("\\"
                                            .concat(twoDigits.replaceAll("^\\d",
                                                    specialChar))).concat("$8$9")));
                        }));

        wrongPhoneNumbers.clear();
        wrongPhoneNumbers.addAll(tempList);

        return wrongPhoneNumbers;
    }

    private static List<String> createCorrectUserNames() {
        List<String> correctNames = new ArrayList<>(List.of(
                "pushkin",
                getRandomString(ENGLISH_LETTERS_AND_NUMBERS, 500)));

        IntStream.range(0, 10).mapToObj(String::valueOf).forEach(correctNames::add);
        List<String> specialChars = getSpecialCharacters("");
        specialChars.addAll(getRussianAndUkrLetters());

        correctNames.addAll(specialChars);

        return correctNames;
    }

    private static List<String> createWrongUserNames() {

        return List.of(
                "",
                " ",
                "www bbb",
                " www",
                "www ");
    }

    private static List<String> createCorrectWordsEmptyOrMin2Max50() {
        List<String> correctSymbols = List.of(
                "w",
                "W",
                "0",
                "9",
                "-",
                "'",
                "`",
                "_"
        );

        List<String> tempList = new ArrayList<>();

        IntStream.range(0, correctSymbols.size()).forEach(i -> {
            for (String correctSymbol : correctSymbols) {
                tempList.add(correctSymbols.get(i).concat(correctSymbol));
            }
        });

        List<String> correctWords = new ArrayList<>(tempList);

        correctWords.forEach(word -> getRussianAndUkrLetters().forEach(
                letter -> tempList.add(word.concat(letter))
        ));

        correctWords.addAll(tempList);
        correctWords.addAll(List.of(
                "",
                getRandomString(ENGLISH_LETTERS_AND_NUMBERS, 50)));

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
                getRandomString(ENGLISH_LETTERS_AND_NUMBERS, 51)));

        List<String> specialChars = getSpecialCharacters("");
        specialChars.removeAll(List.of("_", "-", "`", "'"));

        specialChars.forEach(specialChar -> wrongWords.add(correctWord.concat(specialChar)));

        return wrongWords;
    }

    private static List<String> getSpecialCharacters(String escapedSymbol) {
        List<String> specialCharacters = new ArrayList<>();

        IntStream.range(getAsciiCodeOfChar('!'), getAsciiCodeOfChar('0'))
                .forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));
        IntStream.range(getAsciiCodeOfChar(':'), getAsciiCodeOfChar('A'))
                .forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));
        IntStream.range(getAsciiCodeOfChar('['), getAsciiCodeOfChar('a'))
                .forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));
        IntStream.range(getAsciiCodeOfChar('{'), getAsciiCodeOfChar('\u007F'))
                .forEach(i -> specialCharacters.add(escapedSymbol + ((char) i)));

        return specialCharacters;
    }

    private static List<String> getRussianAndUkrLetters() {
        List<String> russianAndUkrLetters = new ArrayList<>();

        russianAndUkrLetters.add(String.valueOf('Ё'));
        russianAndUkrLetters.add(String.valueOf('Є'));
        IntStream.range(getAsciiCodeOfChar('І'), getAsciiCodeOfChar('Ј'))
                .forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));
        IntStream.range(getAsciiCodeOfChar('А'), getAsciiCodeOfChar('ѐ'))
                .forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));
        russianAndUkrLetters.add(String.valueOf('ё'));
        russianAndUkrLetters.add(String.valueOf('є'));
        IntStream.range(getAsciiCodeOfChar('і'), getAsciiCodeOfChar('ј'))
                .forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));
        IntStream.range(getAsciiCodeOfChar('Ґ'), getAsciiCodeOfChar('Ғ'))
                .forEach(i -> russianAndUkrLetters.add(String.valueOf((char) i)));

        return russianAndUkrLetters;
    }

    private static int getAsciiCodeOfChar(char character) {
        return character;
    }

    private static void addReplacedPhoneNumbers(List<String> replacementList, List<String> tempList,
                                                String replacedString) {
        for (String replacement : replacementList) {
            tempList.add(replacedString.replaceAll(
                    REGROUPED_PHONE_NUMBER_PATTERN, replacement));
        }
    }

    private static String getRandomString(String source, int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(source.charAt(random.nextInt(source.length())));
        }
        return stringBuilder.toString();
    }
}
