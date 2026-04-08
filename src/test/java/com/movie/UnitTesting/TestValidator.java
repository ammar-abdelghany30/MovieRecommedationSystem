package com.movie.UnitTesting;

import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestValidator {
    // TC-T01: Single word, properly capitalized → true
    @Test
    void singleWordCapitalized() {
        assertTrue(Validator.isValidMovieTitle("Inception"));
    }

    // TC-T02: Multiple words, all capitalized → true
    @Test
    void multipleWordsAllCapitalized() {
        assertTrue(Validator.isValidMovieTitle("The Dark Knight"));
    }

    // TC-T03: First word not capitalized → false
    @Test
    void firstWordLowercase() {
        assertFalse(Validator.isValidMovieTitle("the Dark Knight"));
    }

    // TC-T04: Middle word not capitalized → false
    @Test
    void middleWordLowercase() {
        assertFalse(Validator.isValidMovieTitle("The dark Knight"));
    }

    // TC-T05: Last word not capitalized → false
    @Test
    void lastWordLowercase() {
        assertFalse(Validator.isValidMovieTitle("The Dark knight"));
    }

    // TC-T06: All words lowercase → false
    @Test
    void allWordsLowercase() {
        assertFalse(Validator.isValidMovieTitle("the dark knight"));
    }

    // TC-T07: Null input → false
    @Test
    void nullInput() {
        assertFalse(Validator.isValidMovieTitle(null));
    }

    // TC-T08: Empty string → false
    @Test
    void emptyString() {
        assertFalse(Validator.isValidMovieTitle(""));
    }

    // TC-T09: Title starts with a digit — isUpperCase('2') is false → false
    @Test
    void titleStartsWithDigit() {
        assertFalse(Validator.isValidMovieTitle("2001 A Space Odyssey"));
    }

    // TC-T10: Single lowercase letter → false
    @Test
    void singleLowercaseLetter() {
        assertFalse(Validator.isValidMovieTitle("a"));
    }

    // TC-T11: Single uppercase letter → true
    @Test
    void singleUppercaseLetter() {
        assertTrue(Validator.isValidMovieTitle("A"));
    }

    // TC-T12: Leading space
    // Bug: empty token causes StringIndexOutOfBoundsException instead of returning false
    @Test
    void leadingSpace() {
        assertFalse(Validator.isValidMovieTitle(" The Matrix"));
    }

    // TC-T13: Double space between words
    // Bug: empty token causes StringIndexOutOfBoundsException instead of returning false
    @Test
    void doubleSpaceBetweenWords() {
        assertFalse(Validator.isValidMovieTitle("The  Matrix"));
    }

    // TC-T14: Word starts with special character '#' → false
    @Test
    void wordStartsWithSpecialChar() {
        assertFalse(Validator.isValidMovieTitle("The #Matrix"));
    }

    // TC-T15: Only spaces — empty tokens
    // Bug: gives True instead of false
    @Test
    void onlySpaces() {
        assertFalse(Validator.isValidMovieTitle("   "));
    }

    // TC-L01: Single-word title, correct initial → true
    @Test
    void singleWordCorrect() {
        assertTrue(Validator.isValidMovieIdLetters("Inception", "I123"));
    }

    // TC-L02: Multi-word title, correct initials → true
    @Test
    void multiWordCorrect() {
        assertTrue(Validator.isValidMovieIdLetters("The Dark Knight", "TDK123"));
    }

    // TC-L03: One letter wrong in ID → false
    @Test
    void oneLetterWrong() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "TDX123"));
    }

    // TC-L04: Missing one initial in ID → false
    @Test
    void missingOneLetter() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "TD123"));
    }

    // TC-L05: Extra letter appended to ID → false
    @Test
    void extraLetterInId() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "TDKX123"));
    }

    // TC-L06: Letters in wrong order → false
    @Test
    void lettersWrongOrder() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "KDT123"));
    }

    // TC-L07: Lowercase letters in ID — "tdk" != "TDK" → false
    @Test
    void lowercaseLettersInId() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "tdk123"));
    }

    // TC-L08: Digits-only ID, no letters — idLetters = "" != "TDK" → false
    @Test
    void digitsOnlyId() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "123"));
    }

    // TC-L09: Letters-only ID (no digits) — this method only checks letters, so → true
    // Digit absence is caught separately by isValidMovieIdNumbers()
    @Test
    void lettersOnlyId() {
        assertTrue(Validator.isValidMovieIdLetters("The Dark Knight", "TDK"));
    }

    // TC-L10: Hyphens survive digit-strip — "T-D-K" != "TDK" → false
    @Test
    void hyphensInId() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "T-D-K123"));
    }

    // TC-L11: Single-word title, wrong → false
    @Test
    void singleWordWrong() {
        assertFalse(Validator.isValidMovieIdLetters("Inception", "X123"));
    }

    // TC-L12: Duplicated letter — "TTDK" != "TDK" → false
    @Test
    void duplicatedLetter() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "TTDK123"));
    }

    // TC-L13: Empty ID string — idLetters = "" != "I" → false
    @Test
    void emptyId() {
        assertFalse(Validator.isValidMovieIdLetters("Inception", ""));
    }

    // TC-L14: Digits embedded between letters — invalid format per requirements → false
    // Bug: method strips all digits and compares only letters, so it incorrectly returns true
    // The method should enforce that all letters appear before any digits
    @Test
    void digitsEmbeddedBetweenLetters() {
        assertFalse(Validator.isValidMovieIdLetters("The Dark Knight", "T1D2K3"));
    }

    // TC-L15: Five-word title, correct → true
    @Test
    void fiveWordTitleCorrect() {
        assertTrue(Validator.isValidMovieIdLetters(
                "Harry Potter And The Goblet", "HPATG123"));
    }
}