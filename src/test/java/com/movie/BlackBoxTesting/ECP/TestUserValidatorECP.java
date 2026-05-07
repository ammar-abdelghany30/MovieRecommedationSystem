package com.movie.BlackBoxTesting.ECP;

import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================================
 *              EQUIVALENCE CLASS PARTITIONING — User Validators
 * ============================================================================
 *
 *  isValidUsername(username)
 *  -------------------------
 *   Valid classes:
 *     EC1  : All lowercase letters                 e.g. "john"
 *     EC2  : All uppercase letters                 e.g. "JOHN"
 *     EC3  : Mixed-case letters                    e.g. "John"
 *     EC4  : Letters with internal spaces          e.g. "John Smith"
 *     EC5  : Single alphabetic character           e.g. "A"
 *
 *   Invalid classes:
 *     EC6  : null
 *     EC7  : Empty string
 *     EC8  : Starts with space                     e.g. " John"
 *     EC9  : Contains digits                       e.g. "John123"
 *     EC10 : Contains special characters           e.g. "John!"
 *     EC11 : Only digits                           e.g. "12345"
 *     EC12 : Only special characters               e.g. "!!!"
 *
 *
 *  isValidUserId(userId)
 *  ---------------------
 *   Valid classes:
 *     EC1 : 9 digits                               "123456789"
 *     EC2 : 8 digits + 1 trailing letter           "12345678A"
 *
 *   Invalid classes:
 *     EC3 : null
 *     EC4 : Empty string
 *     EC5 : Length < 9                             "12345"
 *     EC6 : Length > 9                             "1234567890AB"
 *     EC7 : Starts with letter                     "A12345678"
 *     EC8 : Letter present but not at the end      "1234A6789"
 *     EC9 : More than one letter                   "1234567AB"
 *     EC10: Contains special characters            "12345678!"
 *     EC11: All letters                            "ABCDEFGHI"
 * ============================================================================
 */
public class TestUserValidatorECP {

    // ========== isValidUsername — VALID classes ==========

    @Test
    void testUsernameValid_AllLowercase() {
        assertTrue(Validator.isValidUsername("john"));
    }

    @Test
    void testUsernameValid_AllUppercase() {
        assertTrue(Validator.isValidUsername("JOHN"));
    }

    @Test
    void testUsernameValid_MixedCase() {
        assertTrue(Validator.isValidUsername("John"));
    }

    @Test
    void testUsernameValid_WithInternalSpaces() {
        assertTrue(Validator.isValidUsername("John Smith"));
    }

    @Test
    void testUsernameValid_SingleAlphabeticChar() {
        assertTrue(Validator.isValidUsername("A"));
    }

    // ========== isValidUsername — INVALID classes ==========

    @Test
    void testUsernameInvalid_Null() {
        assertFalse(Validator.isValidUsername(null));
    }

    @Test
    void testUsernameInvalid_Empty() {
        assertFalse(Validator.isValidUsername(""));
    }

    @Test
    void testUsernameInvalid_StartsWithSpace() {
        assertFalse(Validator.isValidUsername(" John"));
    }

    @Test
    void testUsernameInvalid_ContainsDigits() {
        assertFalse(Validator.isValidUsername("John123"));
    }

    @Test
    void testUsernameInvalid_ContainsSpecialChars() {
        assertFalse(Validator.isValidUsername("John!"));
    }

    @Test
    void testUsernameInvalid_OnlyDigits() {
        assertFalse(Validator.isValidUsername("12345"));
    }

    @Test
    void testUsernameInvalid_OnlySpecialChars() {
        assertFalse(Validator.isValidUsername("!!!"));
    }

    // ========== isValidUserId — VALID classes ==========

    @Test
    void testUserIdValid_NineDigits() {
        assertTrue(Validator.isValidUserId("123456789"));
    }

    @Test
    void testUserIdValid_EightDigitsTrailingLetter() {
        assertTrue(Validator.isValidUserId("12345678A"));
    }

    // ========== isValidUserId — INVALID classes ==========

    @Test
    void testUserIdInvalid_Null() {
        assertFalse(Validator.isValidUserId(null));
    }

    @Test
    void testUserIdInvalid_Empty() {
        assertFalse(Validator.isValidUserId(""));
    }

    @Test
    void testUserIdInvalid_TooShort() {
        assertFalse(Validator.isValidUserId("12345"));
    }

    @Test
    void testUserIdInvalid_TooLong() {
        assertFalse(Validator.isValidUserId("1234567890AB"));
    }

    @Test
    void testUserIdInvalid_StartsWithLetter() {
        assertFalse(Validator.isValidUserId("A12345678"));
    }

    @Test
    void testUserIdInvalid_LetterNotAtEnd() {
        assertFalse(Validator.isValidUserId("1234A6789"));
    }

    @Test
    void testUserIdInvalid_MultipleLetters() {
        assertFalse(Validator.isValidUserId("1234567AB"));
    }

    @Test
    void testUserIdInvalid_ContainsSpecialChar() {
        assertFalse(Validator.isValidUserId("12345678!"));
    }

    @Test
    void testUserIdInvalid_AllLetters() {
        assertFalse(Validator.isValidUserId("ABCDEFGHI"));
    }
}