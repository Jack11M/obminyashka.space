package com.hillel.items_exchange.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hillel.items_exchange.util.PatternHandler.EMAIL;
import static com.hillel.items_exchange.util.PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PatternHandlerTest {

    @ParameterizedTest
    @MethodSource("createCorrectEmails")
    public void testEmailRegexp_whenEmailIsCorrect(String correctEmail) {
        assertTrue(correctEmail.matches(EMAIL));
    }

    @ParameterizedTest
    @MethodSource("createIncorrectEmails")
    public void testEmailRegexp_whenEmailIsNotCorrect(String incorrectEmail) {
        assertFalse(incorrectEmail.matches(EMAIL));
    }

    @Test
    public void testWordEmptyOrMin2Max50_isCorrect() {
        assertTrue("".matches(WORD_EMPTY_OR_MIN_2_MAX_50));
    }

    private static List<String> createCorrectEmails() {
        List<String> correctEmails = new ArrayList<>();
        correctEmails.add("pushkin@ukr.net");
        correctEmails.add("pushkin.145@gmail.com");
        correctEmails.add("pushkin@mail.ru");
        correctEmails.add("pushkin.pushkin@mail.ru");
        correctEmails.add("pushkin.pushkin@mail.mail.ru");

        return correctEmails;
    }

    private static List<String> createIncorrectEmails() {
        List<String> incorrectEmails = new ArrayList<>();
        incorrectEmails.add("pushkin@ukr.n");
        incorrectEmails.add(".145@gmail.com");
        incorrectEmails.add("pushkin@@mail.ru");
        incorrectEmails.addAll(getStringsWithIncorrectChars("pu", "shkin@ukr.net"));
        incorrectEmails.addAll(getStringsWithRussianChars("pu", "shkin@ukr.net"));

        return incorrectEmails;
    }

    private static List<String> getStringsWithIncorrectChars(String str1, String str2) {

        List<Character> incorrectChars = List.of('`', '!', ',', '#', '$', '%', '^', '&', '*', '(', ')', '=', '\'', '"', ':', ';', '/', '\\', '|', '?');

        return incorrectChars.stream()
                .map(character -> str1.concat(String.valueOf(character)).concat(str2))
                .collect(Collectors.toList());
    }

    private static List<String> getStringsWithRussianChars(String str1, String str2) {
        List<Character> russianChars = List.of('ы', 'ь', 'п', 'а', 'С', 'ё');
        return russianChars.stream()
                .map(character -> str1.concat(String.valueOf(character)).concat(str2))
                .collect(Collectors.toList());
    }
}