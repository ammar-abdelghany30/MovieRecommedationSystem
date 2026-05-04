package com.movie.IntegrationTest;

import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestFileReader_Validator_BottomUp {

    // ----- isValidMovieTitle -----
    @Test
    void isValidMovieTitle_Valid_Correct() {
        assertTrue(Validator.isValidMovieTitle("The Matrix"));
        assertTrue(Validator.isValidMovieTitle("Inception"));
        assertTrue(Validator.isValidMovieTitle("Star Wars The Last Jedi"));
    }

    @Test
    void isValidMovieTitle_Invalid_ReturnsFalse() {
        assertFalse(Validator.isValidMovieTitle("the Matrix"));   // first letter lowercase
        assertFalse(Validator.isValidMovieTitle("The matrix"));   // inner word lowercase
        assertFalse(Validator.isValidMovieTitle(""));             // empty
        assertFalse(Validator.isValidMovieTitle(null));           // null
    }

    // ----- isValidMovieIdLetters -----
    @Test
    void isValidMovieIdLetters_Valid_True() {
        assertTrue(Validator.isValidMovieIdLetters("The Matrix", "TM123"));
        assertTrue(Validator.isValidMovieIdLetters("Star Wars", "SW999"));
    }

    @Test
    void isValidMovieIdLetters_Invalid_False() {
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "TX123"));
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "12345"));
    }

    // ----- isValidMovieIdNumbers -----
    @Test
    void isValidMovieIdNumbers_Valid_True() {
        assertTrue(Validator.isValidMovieIdNumbers("TM123"));
        assertTrue(Validator.isValidMovieIdNumbers("SW987"));
    }

    @Test
    void isValidMovieIdNumbers_Invalid_False() {
        assertFalse(Validator.isValidMovieIdNumbers("TM111"));  // digits not unique
        assertFalse(Validator.isValidMovieIdNumbers("TM12"));   // <3 digits
        assertFalse(Validator.isValidMovieIdNumbers("TM1234")); // >3 digits
    }

    // ----- isValidCategory -----
    @Test
    void isValidCategory_Valid_True() {
        assertTrue(Validator.isValidCategory("action"));
        assertTrue(Validator.isValidCategory("  SCI-FI  "));
    }

    // ----- isValidUsername -----
    @Test
    void isValidUsername_Valid_True() {
        assertTrue(Validator.isValidUsername("John Smith"));
        assertTrue(Validator.isValidUsername("Alice"));
        assertTrue(Validator.isValidUsername("a b c"));
    }

    @Test
    void isValidUsername_Invalid_False() {
        assertFalse(Validator.isValidUsername(" John"));   // starts with space
        assertFalse(Validator.isValidUsername("John2"));   // contains digit
        assertFalse(Validator.isValidUsername("John@Smith")); // special char
        assertFalse(Validator.isValidUsername(""));        // empty
    }

    // ----- isValidUserId -----
    @Test
    void isValidUserId_Valid_True() {
        assertTrue(Validator.isValidUserId("123456789"));      // all digits
        assertTrue(Validator.isValidUserId("12345678A"));      // 8 digits + 1 letter at end
        assertTrue(Validator.isValidUserId("12345678z"));      // lowercase letter ok
    }

    @Test
    void isValidUserId_Invalid_False() {
        assertFalse(Validator.isValidUserId("12345678"));      // too short
        assertFalse(Validator.isValidUserId("1234567890"));    // too long
        assertFalse(Validator.isValidUserId("A12345678"));     // starts with letter
        assertFalse(Validator.isValidUserId("1234A5678"));     // letter in middle
        assertFalse(Validator.isValidUserId("1234567AB"));     // two letters
        assertFalse(Validator.isValidUserId("12345678@"));     // special char
    }
}