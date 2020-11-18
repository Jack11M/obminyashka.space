package com.hillel.items_exchange.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.hillel.items_exchange.util.PatternHandler.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The class represents tests for all {@link com.hillel.items_exchange.util.PatternHandler}
 * PatternHandlers.
 *
 * Some ranges of ascii character codes to get them from {@link IntStream}
 * IntStream.range(int startInclusive, int endExclusive):
 *
 * Pairs of characters to get all special characters
 * '!', '0'
 * ':', 'A'
 * '[', 'a'
 * '{', '\u007F'
 *
 * Pairs of characters to get all russian and ukrainian letters
 * 'Ё', 'Ђ'
 * 'Є', 'Ѕ'
 * 'І', 'Ј'
 * 'А', 'ѐ'
 * 'ё', 'ђ'
 * 'є', 'ѕ'
 * 'і', 'ј'
 * 'Ґ', 'Ғ'
 *
 * Pair of characters to get all numbers (0 - 9)
 * '0', ':'
 *
 * Pair of characters to get all english letters (uppercase and lowercase)
 * 'A', '['  //uppercase
 * 'a', '{'  //lowercase
 */
class PatternHandlerTest {

    private static final String REGROUPED_PHONE_NUMBER_PATTERN =
            "^\\s*(?<country>\\+?\\d{2})([-. (]*)(?<area>\\d{3})([-. )]*)(\\d{3})([-. ]*)(\\d{2})([-. ]*)(\\d{2})\\s*$";
    private static final String REGROUPED_EMAIL_PATTERN =
            "^([\\w-+]+)(\\.[\\w]+)*(@)([\\w-]+)(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";
    private static final String REGROUPED_PASSWORD_PATTERN =
            "(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(?=\\S+$)([\\w\\p{Punct}]+)";
    public static final String
            REGROUPED_WORD_EMPTY_OR_MIN_2_MAX_50 = "^($|^[\\wА-Яа-я-'`ЁёҐЄІЇієїґ]{2,50})$";
    private static final String ENGLISH_LETTERS_AND_NUMBERS = getStringFromStringsList(
            getAnySymbols(
                    "",
                    Collections.emptyList(),
                    new CustomPair<>('0', ':'),
                    new CustomPair<>('A', '['),
                    new CustomPair<>('0', ':'),
                    new CustomPair<>('a', '{')));

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

        List<String> specialCharsAndRusUkrLetters = getAnySymbols(
                "\\",
                List.of("\\.", "\\_", "\\+", "\\-"),
                new CustomPair<>('!', '0'),
                new CustomPair<>(':', 'A'),
                new CustomPair<>('[', 'a'),
                new CustomPair<>('{', '\u007F'),
                new CustomPair<>('Ё', 'Ђ'),
                new CustomPair<>('Є', 'Ѕ'),
                new CustomPair<>('І', 'Ј'),
                new CustomPair<>('А', 'ѐ'),
                new CustomPair<>('ё', 'ђ'),
                new CustomPair<>('є', 'ѕ'),
                new CustomPair<>('і', 'ј'),
                new CustomPair<>('Ґ', 'Ғ'));

        addReplacedStringsWithWantedSymbols(
                wrongEmails,
                specialCharsAndRusUkrLetters,
                createCorrectEmails(),
                REGROUPED_EMAIL_PATTERN,
                "$1", "$2$3$4$5$6");

        return wrongEmails;
    }

    private static List<String> createCorrectPasswords() {
        List<String> correctPasswords = new ArrayList<>();
        String minCorrectPassword = "aA1";
        correctPasswords.add(minCorrectPassword);

        final List<String> specialChars = getAnySymbols(
                "\\",
                Collections.emptyList(),
                new CustomPair<>('!', '0'),
                new CustomPair<>(':', 'A'),
                new CustomPair<>('[', 'a'),
                new CustomPair<>('{', '\u007F'));

        List<String> tempList = new ArrayList<>();

        addReplacedStringsWithWantedSymbols(
                tempList,
                specialChars,
                correctPasswords,
                REGROUPED_PASSWORD_PATTERN,
                "", "$1");

        correctPasswords.addAll(tempList);

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
        for (int i = 0; i < wrongPasswords.size(); i++) {
            for (String wrongPassword : wrongPasswords) {
                tempList.add(wrongPasswords.get(i).concat(wrongPassword));
                tempList.add(wrongPasswords.get(i).concat(wrongPassword).concat(wrongPasswords.get(i)));
            }
        }

        wrongPasswords.addAll(tempList);

        wrongPasswords.addAll(List.of(
                minCorrectPassword.concat(" "),
                " ".concat(minCorrectPassword),
                minCorrectPassword.concat(" A"),
                "A ".concat(minCorrectPassword)
        ));

        List<String> russianAndUkrainianSymbols = getAnySymbols("",
                Collections.emptyList(),
                new CustomPair<>('Ё', 'Ђ'),
                new CustomPair<>('Є', 'Ѕ'),
                new CustomPair<>('І', 'Ј'),
                new CustomPair<>('А', 'ѐ'),
                new CustomPair<>('ё', 'ђ'),
                new CustomPair<>('є', 'ѕ'),
                new CustomPair<>('і', 'ј'),
                new CustomPair<>('Ґ', 'Ғ'));

        addReplacedStringsWithWantedSymbols(
                wrongPasswords,
                russianAndUkrainianSymbols,
                createCorrectPasswords(),
                REGROUPED_PASSWORD_PATTERN,
                "", "$1");

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
        List<String> replacementList = getCorrectPhoneNumbersReplacementList();

        List<String> tempList = new ArrayList<>();
        for (String correctNumber : correctPhoneNumbers) {
            addReplacedPhoneNumbers(replacementList, tempList, correctNumber);
        }
        correctPhoneNumbers.addAll(tempList);

        return correctPhoneNumbers;
    }

    private static List<String> createWrongPhoneNumbers() {
        List<String> correctPhoneNumbers = getCorrectPhoneNumbers();
        List<String> replacementList = getWrongPhoneNumbersReplacementList();

        List<String> wrongPhoneNumbers = new ArrayList<>();

        for (String wrongNumber : correctPhoneNumbers) {
            addReplacedPhoneNumbers(replacementList, wrongPhoneNumbers, wrongNumber);
        }

        List<String> specialChars = getAnySymbols(
                "\\",
                List.of("\\.", "\\-"),
                new CustomPair<>('!', '0'),
                new CustomPair<>(':', 'A'),
                new CustomPair<>('[', 'a'),
                new CustomPair<>('{', '\u007F'));

        addReplacedStringsWithWantedSymbols(
                wrongPhoneNumbers,
                specialChars,
                correctPhoneNumbers,
                REGROUPED_PHONE_NUMBER_PATTERN,
                "$1$2$3$4$5", "$7$8$9"
        );

        specialChars.forEach(
                specialChar -> correctPhoneNumbers.forEach(
                        number -> {
                            String twoDigits = number
                                    .replaceAll(REGROUPED_PHONE_NUMBER_PATTERN, "$7");

                            wrongPhoneNumbers.add(number.replaceAll(REGROUPED_PHONE_NUMBER_PATTERN,
                                    "$1$2$3$4$5$6".concat(
                                            "\\".concat(twoDigits.replaceAll("^\\d",
                                                    specialChar))).concat("$8$9")));
                        }));

        return wrongPhoneNumbers;
    }

    private static List<String> createCorrectUserNames() {
        List<String> correctNames = new ArrayList<>(List.of(
                "pushkin",
                getRandomString(ENGLISH_LETTERS_AND_NUMBERS, 500)));

        IntStream.range(0, 10).mapToObj(String::valueOf).forEach(correctNames::add);

        List<String> specialChars = getAnySymbols(
                "",
                Collections.emptyList(),
                new CustomPair<>('!', '0'),
                new CustomPair<>(':', 'A'),
                new CustomPair<>('[', 'a'),
                new CustomPair<>('{', '\u007F'));

        //add russian and ukr letters
        specialChars.addAll(getAnySymbols("",
                Collections.emptyList(),
                new CustomPair<>('Ё', 'Ђ'),
                new CustomPair<>('Є', 'Ѕ'),
                new CustomPair<>('І', 'Ј'),
                new CustomPair<>('А', 'ѐ'),
                new CustomPair<>('ё', 'ђ'),
                new CustomPair<>('є', 'ѕ'),
                new CustomPair<>('і', 'ј'),
                new CustomPair<>('Ґ', 'Ғ')));

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

        for (int i = 0; i < correctSymbols.size(); i++) {
            for (String correctSymbol : correctSymbols) {
                tempList.add(correctSymbols.get(i).concat(correctSymbol));
            }
        }

        List<String> correctWords = new ArrayList<>(tempList);

        List<String> russianAndUkrainianLetters = getAnySymbols("",
                Collections.emptyList(),
                new CustomPair<>('Ё', 'Ђ'),
                new CustomPair<>('Є', 'Ѕ'),
                new CustomPair<>('І', 'Ј'),
                new CustomPair<>('А', 'ѐ'),
                new CustomPair<>('ё', 'ђ'),
                new CustomPair<>('є', 'ѕ'),
                new CustomPair<>('і', 'ј'),
                new CustomPair<>('Ґ', 'Ғ'));

        addReplacedStringsWithWantedSymbols(
                correctWords,
                russianAndUkrainianLetters,
                tempList,
                REGROUPED_WORD_EMPTY_OR_MIN_2_MAX_50,
                "$1", ""
        );

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
                " "));

        ArrayList<String> tempList = new ArrayList<>();

        for (int i = 0; i < wrongWords.size(); i++) {
            for (String str : wrongWords) {
                String concat = str.concat(wrongWords.get(i));
                if (concat.contains(" ")) {
                    tempList.add(concat);
                }
            }
        }

        wrongWords.addAll(tempList);

        wrongWords.add(getRandomString(ENGLISH_LETTERS_AND_NUMBERS, 51));

        List<String> specialChars = getAnySymbols(
                "",
                List.of("_", "-", "`", "'"),
                new CustomPair<>('!', '0'),
                new CustomPair<>(':', 'A'),
                new CustomPair<>('[', 'a'),
                new CustomPair<>('{', '\u007F'));

        specialChars.forEach(specialChar -> wrongWords.add(correctWord.concat(specialChar)));

        return wrongWords;
    }

    @SafeVarargs
    private static List<String> getAnySymbols(String escapedSymbol,
                                              List<String> deletedSymbols,
                                              CustomPair<Character, Character>... pairs) {
        List<String> specialCharacters = new ArrayList<>();

        for (CustomPair<Character, Character> pair : pairs) {
            addCharactersToList(escapedSymbol, specialCharacters, pair.getT(), pair.getT1());
        }

        specialCharacters.removeAll(deletedSymbols);

        return specialCharacters;
    }

    private static void addCharactersToList(String escapedSymbol, List<String> characters,
                                            char char1, char char2) {
        IntStream.range(getAsciiCodeOfChar(char1), getAsciiCodeOfChar(char2))
                .forEach(i -> characters.add(escapedSymbol + ((char) i)));
    }

    private static String getStringFromStringsList(List<String> stringList) {
        String string = "";
        for (String s : stringList) {
            string = string.concat(s);
        }
        return string;
    }

    private static int getAsciiCodeOfChar(char character) {
        return character;
    }

    private static void addReplacedPhoneNumbers(List<String> replacementList,
                                                List<String> listForAdding,
                                                String replacedString) {
        for (String replacement : replacementList) {
            listForAdding.add(replacedString.replaceAll(
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

    private static void addReplacedStringsWithWantedSymbols(List<String> resultList,
                                                            List<String> addedSymbols,
                                                            List<String> listForAdding,
                                                            String regroupedPasswordPattern,
                                                            String firstPartForConcat,
                                                            String lastPartForConcat) {
        addedSymbols.forEach(symbol -> listForAdding
                .forEach(changingSymbol -> resultList.add(
                        changingSymbol.replaceAll(
                                regroupedPasswordPattern,
                                firstPartForConcat.concat(symbol).concat(lastPartForConcat))
                )));
    }

    private static List<String> getCorrectPhoneNumbersReplacementList() {
        return List.of(
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
    }

    private static List<String> getWrongPhoneNumbersReplacementList() {
        return List.of(
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
    }
}
