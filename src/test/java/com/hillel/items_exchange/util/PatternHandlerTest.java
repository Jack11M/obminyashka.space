package com.hillel.items_exchange.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.util.PatternHandler.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternHandlerTest {

    @ParameterizedTest
    @MethodSource("createCorrectEmails")
    void testEmailRegexp_whenEmailIsCorrect(String correctEmail) {
        assertTrue(correctEmail.matches(EMAIL));
    }

    @ParameterizedTest
    @MethodSource("createIncorrectEmails")
    void testEmailRegexp_whenEmailIsNotCorrect(String incorrectEmail) {
        assertFalse(incorrectEmail.matches(EMAIL));
    }

    @ParameterizedTest
    @MethodSource("createCorrectNames")
    void testUsernameRegexp_whenUsernameIsCorrect(String correctUserName) {
        assertTrue(correctUserName.matches(USERNAME));
    }

    @ParameterizedTest
    @MethodSource("createIncorrectNames")
    void testUsernameRegexp_whenUsernameIsNotCorrect(String incorrectUserName) {
        assertFalse(incorrectUserName.matches(USERNAME));
    }

    @ParameterizedTest
    @MethodSource("createCorrectWordsEmptyOrMin2Max50")
    void testWordEmptyOrMin2Max50_isCorrect(String correctWord) {
        assertTrue(correctWord.matches(WORD_EMPTY_OR_MIN_2_MAX_50));
    }

    @ParameterizedTest
    @MethodSource("createIncorrectWordsEmptyOrMin2Max50")
    void testWordEmptyOrMin2Max50_isIncorrect(String incorrectWord) {
        assertFalse(incorrectWord.matches(WORD_EMPTY_OR_MIN_2_MAX_50));
    }

    private static List<String> createCorrectEmails() {
        List<String> correctEmails = new ArrayList<>();
        correctEmails.add("pushkin@ukr.net");
        correctEmails.add("pushkin.145@gmail.com");
        correctEmails.add("pushkin@mail.ru");
        correctEmails.add("pushkin.pushkin@mail.ru");
        correctEmails.add("pushkin.pushkin@mail.mail.ru");
        correctEmails.add("1@ukr.net");
        correctEmails.add("1@111.ru");

        return correctEmails;
    }

    private static List<String> createIncorrectEmails() {
        List<String> incorrectEmails = new ArrayList<>();
        incorrectEmails.add("pushkin@ukr.n");
        incorrectEmails.add(".145@gmail.com");
        incorrectEmails.add("pushkin@@mail.ru");
        incorrectEmails.add("1@111.11");
        incorrectEmails.add("1@111.1u");
        incorrectEmails.add("1@111.1ru");
        incorrectEmails.add("1@111.ru1");
        incorrectEmails.add("1@ma~il.ru");

        List<Character> correctCharacters = List.of('.', '_', '+');

        incorrectEmails.addAll(getStringsWithIncorrectChars("pu", "shkin@ukr.net", correctCharacters));
        incorrectEmails.addAll(getStringsWithRussianAndUkrCharacters("pu", "shkin@ukr.net"));

        return incorrectEmails;
    }

    private static List<String> createCorrectNames() {
        List<String> correctNames = new ArrayList<>();
        correctNames.add("pushkin");
        correctNames.add("ThisNameHasLengthMoreThan100SymbolsThisNameHasLengthMoreThan100SymbolsThisNameHasLengthMoreThan100Symbols");
        correctNames.add("~~``~~");
        correctNames.add("'@'\"`!");
        correctNames.add("1");

        //In this case, there are no wrong characters
        correctNames.addAll(getStringsWithIncorrectChars("pu", "shkin", Collections.emptyList()));
        correctNames.addAll(getStringsWithRussianAndUkrCharacters("aaa", "bbb"));

        return correctNames;
    }

    private static List<String> createIncorrectNames() {
        List<String> incorrectNames = new ArrayList<>();
        incorrectNames.add("");
        incorrectNames.add(" ");
        incorrectNames.add("aaa bbb");
        incorrectNames.add(" aaa");
        incorrectNames.add("aaa ");

        return incorrectNames;
    }

    private static List<String> createCorrectWordsEmptyOrMin2Max50() {
        List<String> correctWords = new ArrayList<>();
        correctWords.add("");
        correctWords.add("aA");
        correctWords.add("11");
        correctWords.add("aA1");
        correctWords.add("aA1-");
        correctWords.add("aA1'");
        correctWords.add("aA1'a");
        correctWords.add("aA1-a");
        correctWords.add("LengthIs50LengthIs50LengthIs50LengthIs50LengthIs50");

        return correctWords;
    }

    private static List<String> createIncorrectWordsEmptyOrMin2Max50() {
        List<String> incorrectWords = new ArrayList<>();
        incorrectWords.add("a");
        incorrectWords.add("1");
        incorrectWords.add("ThisWordHasLengthMoreThan50CharactersItsLengthIs51S");

        List<Character> correctCharacters = List.of('\'', '_', '`');
        incorrectWords.addAll(getStringsWithIncorrectChars("aaa", "bbb", correctCharacters));

        return incorrectWords;
    }

    private static List<String> getStringsWithIncorrectChars(String str1, String str2, List<Character> correctCharacters) {
        List<Character> incorrectChars = new ArrayList<>(List.of('`', '!', '@', '#', '$', '%', '^', '&', '*', '(',
                ')', '+', '=', '[', ']', '{', '}', '\'', '"', ':', ';', '/', '\\', '|', '?', '.', ',', '_'));
        incorrectChars.removeAll(correctCharacters);

        return incorrectChars.stream()
                .map(character -> str1.concat(String.valueOf(character)).concat(str2))
                .collect(Collectors.toList());
    }

    private static List<String> getStringsWithRussianAndUkrCharacters(String str1, String str2) {
        List<Character> russianChars = List.of('Й', 'й', 'Ц', 'ц', 'У', 'у', 'Г', 'г', 'Ш', 'ш', 'Щ', 'щ', 'З', 'з',
                'Х', 'х', 'Ъ', 'ъ', 'Ф', 'ф', 'Ы', 'ы', 'П', 'п', 'Л', 'л', 'Д', 'д', 'Ж', 'ж', 'Э', 'э', 'Я', 'я',
                'Ч', 'ч', 'М', 'м', 'Ь', 'ь', 'Б', 'б', 'Ю', 'ю', 'Ґ', 'Є', 'І', 'Ї', 'і', 'є', 'ї', 'ґ');

        return russianChars.stream()
                .map(character -> str1.concat(String.valueOf(character)).concat(str2))
                .collect(Collectors.toList());
    }
}