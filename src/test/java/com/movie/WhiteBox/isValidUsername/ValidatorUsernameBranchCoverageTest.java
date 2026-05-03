package com.movie.WhiteBox.isValidUsername;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Branch Coverage tests for Validator.isValidUsername.
 *
 * Goal: exercise every true/false edge out of every decision node.
 *   3 decisions x 2 outcomes = 6 branches, covered by 4 tests.
 *
 * Branch coverage matrix:
 *
 *   TC-U1  null        -> B1-T
 *   TC-U2  " John"     -> B1-F, B2-T
 *   TC-U3  "John Doe"  -> B1-F, B2-F, B3-T
 *   TC-U4  "John123"   -> B1-F, B2-F, B3-F
 *
 * All 6 branches covered => 100% branch coverage.
 */
@DisplayName("isValidUsername - Branch Coverage")
public class ValidatorUsernameBranchCoverageTest {

    @Test
    @DisplayName("TC-U1: null username -> B1-T (N2 true) -> N3")
    void tcU1_nullUsername_returnsFalse() {
        // B1-T: N2 evaluates true (short-circuit on null) -> goes to N3
        assertFalse(Validator.isValidUsername(null));
    }

    @Test
    @DisplayName("TC-U2: leading space -> B1-F (N2 false), B2-T (N4 true) -> N5")
    void tcU2_leadingSpace_returnsFalse() {
        // B1-F: N2 evaluates false -> falls to N4
        // B2-T: N4 evaluates true (first char is space) -> goes to N5
        assertFalse(Validator.isValidUsername(" John"));
    }

    @Test
    @DisplayName("TC-U3: valid username -> B1-F, B2-F (N4 false), B3-T (N6 true) -> N7")
    void tcU3_validUsername_returnsTrue() {
        // B1-F: N2 false -> N4
        // B2-F: N4 false (first char is not space) -> N6
        // B3-T: N6 true (all chars match [a-zA-Z ]+) -> N7
        assertTrue(Validator.isValidUsername("John Doe"));
    }

    @Test
    @DisplayName("TC-U4: invalid characters -> B1-F, B2-F, B3-F (N6 false) -> N8")
    void tcU4_invalidCharacters_returnsFalse() {
        // B1-F: N2 false -> N4
        // B2-F: N4 false -> N6
        // B3-F: N6 false (digits don't match [a-zA-Z ]+) -> N8
        assertFalse(Validator.isValidUsername("John123"));
    }
}