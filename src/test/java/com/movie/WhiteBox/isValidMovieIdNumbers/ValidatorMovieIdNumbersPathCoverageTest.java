package com.movie.WhiteBox.isValidMovieIdNumbers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Path Coverage tests for Validator.isValidMovieIdNumbers.
 * Goal: execute every possible path through the CFG.
 */
@DisplayName("isValidMovieIdNumbers - Path Coverage")
public class ValidatorMovieIdNumbersPathCoverageTest {

    @Test
    @DisplayName("Path 1: M1-M2-M3-M4")
    void path1() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC1"));
    }

    @Test
    @DisplayName("Path 2: M1-M2-M3-M5-M9")
    void path2() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC111"));
    }

    @Test
    @DisplayName("Path 3: M1-M2-M3-M5-M6-M9")
    void path3() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC122"));
    }

    @Test
    @DisplayName("Path 4: M1-M2-M3-M5-M6-M7-M9")
    void path4() {
        assertFalse(Validator.isValidMovieIdNumbers("ABC121"));
    }

    @Test
    @DisplayName("Path 5: M1-M2-M3-M5-M6-M7-M8")
    void path5() {
        assertTrue(Validator.isValidMovieIdNumbers("ABC123"));
    }
}
