package com.movie.BlackBoxTesting;

import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestUserBVA {

    // UserID length boundaries
    @Test
    void testUserIdLength8() {
        assertFalse(Validator.isValidUserId("12345678"), "8 chars -> false (below boundary)");
    }

    @Test
    void testUserIdLength9() {
        assertTrue(Validator.isValidUserId("123456789"), "9 chars -> true (exact boundary)");
    }

    @Test
    void testUserIdLength10() {
        assertFalse(Validator.isValidUserId("1234567890"), "10 chars -> false (above boundary)");
    }

    // Letter count at end
    @Test
    void testUserId0Letters() {
        assertTrue(Validator.isValidUserId("123456789"), "0 letters -> true");
    }

    @Test
    void testUserId1LetterAtEnd() {
        assertTrue(Validator.isValidUserId("12345678A"), "1 letter at end -> true");
    }

    @Test
    void testUserId1LetterNotAtEnd() {
        assertFalse(Validator.isValidUserId("1234A6789"), "1 letter NOT at end -> false");
    }

    @Test
    void testUserId2Letters() {
        assertFalse(Validator.isValidUserId("1234567AB"), "2 letters -> false");
    }

    // Username boundaries
    @Test
    void testUsername1CharAlphabetic() {
        assertTrue(Validator.isValidUsername("A"), "1 char alphabetic -> true");
    }

    @Test
    void testUsername1CharSpace() {
        assertFalse(Validator.isValidUsername(" "), "1 char space -> false");
    }

    @Test
    void testUsernameStartsWithSpace() {
        assertFalse(Validator.isValidUsername(" A"), "starts with space -> false");
    }
}
