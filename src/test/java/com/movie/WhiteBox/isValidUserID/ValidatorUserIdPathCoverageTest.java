package com.movie.WhiteBox.isValidUserID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Path Coverage tests for Validator.isValidUserId.
 **
 * Goal: execute every complete path from entry to exit at least once.
 *   7 unique paths covered by 8 tests.
 *
 * Cyclomatic complexity: V(G) = 5 decisions + 1 = 6 independent basis paths.
 *
 *
 * Path coverage matrix:
 *
 *   TC-I1  null          -> Q1 (via C1=T)
 *   TC-I2  "12345678"    -> Q1 (via C2=T)
 *   TC-I3  "12345678@"   -> Q2
 *   TC-I4  "a12345678"   -> Q3
 *   TC-I5  "1234567ab"   -> Q4
 *   TC-I6  "1234a5678"   -> Q5
 *   TC-I7  "12345678a"   -> Q6
 *   TC-I8  "123456789"   -> Q7
 *
 * Path definitions:
 *
 *   Q1  M1->M2->M3->M14                                          (null or wrong length)
 *   Q2  M1->M2->M4->M5->M14                                      (non-alphanumeric)
 *   Q3  M1->M2->M4->M6->M7->M14                                  (doesn't start with digit)
 *   Q4  M1->M2->M4->M6->M8->M9->M10->M14                         (2+ letters)
 *   Q5  M1->M2->M4->M6->M8->M9->M11->M12->M14                    (1 letter, not at end)
 *   Q6  M1->M2->M4->M6->M8->M9->M11->M13->M14                    (1 letter at end - valid)
 *   Q7  M1->M2->M4->M6->M8->M9->M11->M13->M14                    (0 letters - valid)
 *
 */
@DisplayName("isValidUserId - Path Coverage")
public class ValidatorUserIdPathCoverageTest {

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