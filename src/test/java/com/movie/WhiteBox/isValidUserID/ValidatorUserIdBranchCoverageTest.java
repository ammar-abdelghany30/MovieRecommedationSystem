package com.movie.WhiteBox.isValidUserID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Branch Coverage tests for Validator.isValidUserId.
 *
 * Member 3 - Software Testing Project
 *
 * Goal: exercise every true/false edge out of every decision node.
 *   5 decisions x 2 outcomes = 10 branches, covered by 6 tests.
 *   (M8 is a statement node, not a decision - it has no true/false edge.)
 *
 * Branch coverage matrix:
 *
 *   TC-I1  null          -> B1-T
 *   TC-I2  "12345678@"   -> B1-F, B2-T
 *   TC-I3  "a12345678"   -> B1-F, B2-F, B3-T
 *   TC-I4  "1234567ab"   -> B1-F, B2-F, B3-F, B4-T
 *   TC-I5  "1234a5678"   -> B1-F, B2-F, B3-F, B4-F, B5-T
 *   TC-I6  "123456789"   -> B1-F, B2-F, B3-F, B4-F, B5-F                                           V
 *
 * All 10 branches covered => 100% branch coverage.
 */
@DisplayName("isValidUserId - Branch Coverage")
public class ValidatorUserIdBranchCoverageTest {

    @Test
    void tcI1_nullUserId_returnsFalse() {
        // B1-T
        assertFalse(Validator.isValidUserId(null));
    }

    @Test
    void tcI2_nonAlphanumeric_returnsFalse() {
        // B1-F
        // B2-T
        assertFalse(Validator.isValidUserId("12345678@"));
    }

    @Test
    void tcI3_startsWithLetter_returnsFalse() {
        // B1-F
        // B2-F
        // B3-T
        assertFalse(Validator.isValidUserId("a12345678"));
    }

    @Test
    void tcI4_tooManyLetters_returnsFalse() {
        // B1-F
        // B2-F
        // B3-F
        // B4-T
        assertFalse(Validator.isValidUserId("1234567ab"));
    }

    @Test
    void tcI5_letterNotAtEnd_returnsFalse() {
        // B1-F
        // B2-F
        // B3-F
        // B4-F
        // B5-T
        assertFalse(Validator.isValidUserId("1234a5678"));
    }

    @Test
    void tcI6_allDigits_returnsTrue() {
        // B1-F
        // B2-F
        // B3-F
        // B4-F
        // B5-F
        assertTrue(Validator.isValidUserId("123456789"));
    }
}