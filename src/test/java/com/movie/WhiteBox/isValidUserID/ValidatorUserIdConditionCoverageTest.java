package com.movie.WhiteBox.isValidUserID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Condition Coverage tests for Validator.isValidUserId.
 *
 * Goal: every atomic condition must independently evaluate to both true and false.
 *   7 atomic conditions covered by 8 tests.
 *
 * Atomic conditions:
 *   C1 - userId == null
 *   C2 - userId.length() != 9
 *   C3 - !userId.matches("[a-zA-Z0-9]+")
 *   C4 - !Character.isDigit(userId.charAt(0))
 *   C5 - letterCount > 1
 *   C6 - letterCount == 1
 *   C7 - !Character.isAlphabetic(userId.charAt(8))
 *
 * Condition coverage matrix:
 *
 *   TC-I1  null          -> C1=T, C2=—
 *   TC-I2  "12345678"    -> C1=F, C2=T
 *   TC-I3  "12345678@"   -> C1=F, C2=F, C3=T
 *   TC-I4  "a12345678"   -> C1=F, C2=F, C3=F, C4=T
 *   TC-I5  "1234567ab"   -> C1=F, C2=F, C3=F, C4=F, C5=T
 *   TC-I6  "1234a5678"   -> C1=F, C2=F, C3=F, C4=F, C5=F, C6=T, C7=T
 *   TC-I7  "12345678a"   -> C1=F, C2=F, C3=F, C4=F, C5=F, C6=T, C7=F
 *   TC-I8  "123456789"   -> C1=F, C2=F, C3=F, C4=F, C5=F, C6=F, C7=—
 *
 * Condition coverage summary:
 *
 *   C1  T=TC-I1   F=TC-I2
 *   C2  T=TC-I2   F=TC-I3
 *   C3  T=TC-I3   F=TC-I4
 *   C4  T=TC-I4   F=TC-I5
 *   C5  T=TC-I5   F=TC-I6
 *   C6  T=TC-I6   F=TC-I8
 *   C7  T=TC-I6   F=TC-I7
 *
 * All 7 atomic conditions see both T and F => 100% condition coverage.
 *
 */
public class ValidatorUserIdConditionCoverageTest {

    @Test
    void tcI1_nullUserId_returnsFalse() {
        assertFalse(Validator.isValidUserId(null));
    }

    @Test
    void tcI2_wrongLength_returnsFalse() {
        assertFalse(Validator.isValidUserId("12345678"));
    }

    @Test
    void tcI3_nonAlphanumeric_returnsFalse() {
        assertFalse(Validator.isValidUserId("12345678@"));
    }

    @Test
    void tcI4_startsWithLetter_returnsFalse() {
        assertFalse(Validator.isValidUserId("a12345678"));
    }

    @Test
    void tcI5_twoLetters_returnsFalse() {
        assertFalse(Validator.isValidUserId("1234567ab"));
    }

    @Test
    void tcI6_letterNotAtEnd_returnsFalse() {
        assertFalse(Validator.isValidUserId("1234a5678"));
    }

    @Test
    void tcI7_letterAtEnd_returnsTrue() {
        assertTrue(Validator.isValidUserId("12345678a"));
    }

    @Test
    void tcI8_allDigits_returnsTrue() {
        assertTrue(Validator.isValidUserId("123456789"));
    }
}