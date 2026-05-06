package com.movie.WhiteBox.isValidMovieIdNumbers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Condition Coverage tests for Validator.isValidMovieIdNumbers.
 * Goal: each atomic condition evaluates to True and False.
 */
@DisplayName("isValidMovieIdNumbers - Condition Coverage")
public class ValidatorMovieIdNumbersConditionCoverageTest {

    @Test
    @DisplayName("Conditions: all True")
    void tcC1_allTrue() {
        assertTrue(Validator.isValidMovieIdNumbers("ABC123"));
    }

    @Test
    @DisplayName("Condition: length != 3")
    void tcC2_lengthCondition() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC1"));
    }

    @Test
    @DisplayName("Condition: first == second")
    void tcC3_firstSecondCondition() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC112"));
    }

    @Test
    @DisplayName("Condition: second == third")
    void tcC4_secondThirdCondition() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC122"));
    }

    @Test
    @DisplayName("Condition: first == third")
    void tcC5_firstThirdCondition() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC121"));
    }
}
