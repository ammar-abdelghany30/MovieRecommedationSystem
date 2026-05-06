package com.movie.WhiteBox.isValidMovieIdNumbers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Statement Coverage tests for Validator.isValidMovieIdNumbers.
 * Goal: execute every statement in isValidMovieIdNumbers at least once.
 * CFG Nodes:
 *   M1: Start
 *   M2: digits = id.replaceAll("[^0-9]", "")
 *   M3: if (digits.length() != 3)
 *   M4: return false
 *   M5: if (digits.charAt(0) != digits.charAt(1))
 *   M6: if (digits.charAt(1) != digits.charAt(2))
 *   M7: if (digits.charAt(0) != digits.charAt(2))
 *   M8: return true
 *   M9: return false (short-circuit)
 */
@DisplayName("isValidMovieIdNumbers - Statement Coverage")
public class ValidatorMovieIdNumbersStatementCoverageTest {

    @Test
    @DisplayName("TC-S1: length != 3 hits M2, M3 -> M4")
    void tcS1_lengthNot3_returnsFalse() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC12"));
    }

    @Test
    @DisplayName("TC-S2: valid ID hits M2, M3, M5, M6, M7 -> M8")
    void tcS2_validId_returnsTrue() {
        assertTrue(Validator.isValidMovieIdNumbers("ABC123"));
    }

    @Test
    @DisplayName("TC-S3: duplicate digits hits M2, M3, M5 -> M9")
    void tcS3_duplicateDigits_returnsFalse() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC112"));
    }
}
