package com.movie.WhiteBox.isValidMovieIdNumbers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Branch Coverage tests for Validator.isValidMovieIdNumbers.
 * Goal: execute every branch (True/False) at least once.
 */
@DisplayName("isValidMovieIdNumbers - Branch Coverage")
public class ValidatorMovieIdNumbersBranchCoverageTest {

    @Test
    @DisplayName("TC-B1: digits.length() != 3 is True")
    void tcB1_lengthNot3_returnsFalse() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC12"));
    }

    @Test
    @DisplayName("TC-B2: All branches True")
    void tcB2_allTrue_returnsTrue() {
        assertTrue(Validator.isValidMovieIdNumbers("ABC123"));
    }

    @Test
    @DisplayName("TC-B3: digits.charAt(0) != digits.charAt(1) is False")
    void tcB3_firstTwoEqual_returnsFalse() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC112"));
    }

    @Test
    @DisplayName("TC-B4: digits.charAt(1) != digits.charAt(2) is False")
    void tcB4_lastTwoEqual_returnsFalse() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC122"));
    }

    @Test
    @DisplayName("TC-B5: digits.charAt(0) != digits.charAt(2) is False")
    void tcB5_firstAndLastEqual_returnsFalse() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC121"));
    }
}
