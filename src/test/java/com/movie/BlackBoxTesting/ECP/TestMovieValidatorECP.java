package com.movie.BlackBoxTesting;

import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestMovieValidatorECP {

    // ========== isValidMovieTitle ==========
    @Test
    void testTitleValid_SingleWord() {
        assertTrue(Validator.isValidMovieTitle("Matrix"));
    }

    @Test
    void testTitleValid_MultipleWords() {
        assertTrue(Validator.isValidMovieTitle("The Lord Of The Rings"));
    }

    @Test
    void testTitleValid_AllCaps() {
        assertTrue(Validator.isValidMovieTitle("MATRIX"));
    }

    @Test
    void testTitleInvalid_FirstWordLowercase() {
        assertFalse(Validator.isValidMovieTitle("the Matrix"));
    }

    @Test
    void testTitleInvalid_InnerWordLowercase() {
        assertFalse(Validator.isValidMovieTitle("The matrix"));
    }

    @Test
    void testTitleInvalid_Empty() {
        assertFalse(Validator.isValidMovieTitle(""));
    }

    @Test
    void testTitleInvalid_Null() {
        assertFalse(Validator.isValidMovieTitle(null));
    }

    @Test
    void testTitleInvalid_MultipleSpaces() {
        assertFalse(Validator.isValidMovieTitle("The  Matrix"));
    }

    @Test
    void testTitleInvalid_LeadingSpace() {
        assertFalse(Validator.isValidMovieTitle(" Matrix"));
    }

    // ========== isValidMovieIdLetters ==========
    @Test
    void testIdLettersValid_ExactMatch() {
        assertTrue(Validator.isValidMovieIdLetters("The Matrix", "TM123"));
    }

    @Test
    void testIdLettersValid_SingleWordTitle() {
        assertTrue(Validator.isValidMovieIdLetters("Matrix", "M987"));
    }

    @Test
    void testIdLettersInvalid_ExtraLetterInId() {
        assertFalse(Validator.isValidMovieIdLetters("Matrix", "MA123"));
    }

    @Test
    void testIdLettersInvalid_FewerLettersInId() {
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "T123"));
    }

    @Test
    void testIdLettersInvalid_MoreLettersInId() {
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "TMA123"));
    }

    @Test
    void testIdLettersInvalid_NoLetters() {
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "12345"));
    }

    @Test
    void testIdLettersInvalid_WrongLetters() {
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "TX123"));
    }

    @Test
    void testIdLettersInvalid_NullTitle() {
        assertFalse(Validator.isValidMovieIdLetters(null, "TM123"));
    }

    // ========== isValidMovieIdNumbers ==========
    @Test
    void testIdNumbersValid_Exactly3UniqueContiguous() {
        assertTrue(Validator.isValidMovieIdNumbers("TM123"));
    }

    @Test
    void testIdNumbersValid_Exactly3UniqueNonContiguous() {
        assertTrue(Validator.isValidMovieIdNumbers("T1M23"));
    }

    @Test
    void testIdNumbersInvalid_DuplicateDigits() {
        assertFalse(Validator.isValidMovieIdNumbers("TM112"));
        assertFalse(Validator.isValidMovieIdNumbers("111"));
    }

    @Test
    void testIdNumbersInvalid_LessThan3Digits() {
        assertFalse(Validator.isValidMovieIdNumbers("TM12"));
    }

    @Test
    void testIdNumbersInvalid_MoreThan3Digits() {
        assertFalse(Validator.isValidMovieIdNumbers("TM1234"));
    }

    @Test
    void testIdNumbersInvalid_NoDigits() {
        assertFalse(Validator.isValidMovieIdNumbers("TM"));
    }

    @Test
    void testIdNumbersInvalid_Empty() {
        assertFalse(Validator.isValidMovieIdNumbers(""));
    }

    @Test
    void testIdNumbersInvalid_Null() {
        assertFalse(Validator.isValidMovieIdNumbers(null));
    }

    @Test
    void testIdNumbersInvalid_NonContiguousDuplicate() {
        assertFalse(Validator.isValidMovieIdNumbers("A1B1C2")); // digits "112"
    }

    // ========== isValidCategory ==========
    @Test
    void testCategoryValid_ExactLower() {
        assertTrue(Validator.isValidCategory("action"));
    }

    @Test
    void testCategoryValid_UpperCase() {
        assertTrue(Validator.isValidCategory("ACTION"));
    }

    @Test
    void testCategoryValid_MixedCase() {
        assertTrue(Validator.isValidCategory("AcTiOn"));
    }

    @Test
    void testCategoryValid_WithSpaces() {
        assertTrue(Validator.isValidCategory("  drama  "));
    }

    @Test
    void testCategoryValid_SciFi() {
        assertTrue(Validator.isValidCategory("sci-fi"));
    }

    @Test
    void testCategoryInvalid_NotInSet() {
        assertFalse(Validator.isValidCategory("fantasy"));
    }

    @Test
    void testCategoryInvalid_Empty() {
        assertFalse(Validator.isValidCategory(""));
    }

    @Test
    void testCategoryInvalid_Null() {
        assertFalse(Validator.isValidCategory(null));
    }

    @Test
    void testCategoryInvalid_SpecialChars() {
        assertFalse(Validator.isValidCategory("act!on"));
    }

    @Test
    void testCategoryInvalid_TrailingPunctuation() {
        assertFalse(Validator.isValidCategory("sci-fi!"));
    }
}