package com.movie.WhiteBox.isValidMovieIdNumbers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Data Flow Coverage tests for Validator.isValidMovieIdNumbers.
 * Goal: exercise Def-Use (DU) pairs for critical variables.
 * Variable: digits
 *   Def: M2 (digits = id.replaceAll("[^0-9]", ""))
 *   Use: M3 (length()), M5 (charAt(0), charAt(1)), M6 (charAt(1), charAt(2)), M7 (charAt(0), charAt(2))
 */
@DisplayName("isValidMovieIdNumbers - Data Flow Coverage")
public class ValidatorMovieIdNumbersDataFlowCoverageTest {

    @Test
    @DisplayName("DU Pair (digits, M2, M3)")
    void testDigitsDefToUseM3() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC1"));
    }

    @Test
    @DisplayName("DU Pair (digits, M2, M5)")
    void testDigitsDefToUseM5() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC112"));
    }

    @Test
    @DisplayName("DU Pair (digits, M2, M6)")
    void testDigitsDefToUseM6() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC122"));
    }

    @Test
    @DisplayName("DU Pair (digits, M2, M7)")
    void testDigitsDefToUseM7() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC121"));
    }
}
