package com.movie.WhiteBox.isValidUsername;

import com.movie.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Path Coverage tests for Validator.isValidUsername.
 *
 * Goal: execute every complete path from entry to exit at least once.
 *   4 unique paths covered by 5 tests.
 *
 * Cyclomatic complexity: V(G) = 3 decisions + 1 = 4 => 4 independent paths.
 *
 * Path coverage matrix:
 *
 *   TC-U1  null        -> P1 (via C1=T)
 *   TC-U2  ""          -> P1 (via C2=T)
 *   TC-U3  " John"     -> P2
 *   TC-U4  "John Doe"  -> P3
 *   TC-U5  "John123"   -> P4
 *
 * Path definitions:
 *
 *   P1  N1 -> N2 -> N3 -> N9   (null or empty - two ways to reach N3)
 *   P2  N1 -> N2 -> N4 -> N5 -> N9   (starts with space)
 *   P3  N1 -> N2 -> N4 -> N6 -> N7 -> N9   (valid username)
 *   P4  N1 -> N2 -> N4 -> N6 -> N8 -> N9   (invalid characters)
 *
 */
public class ValidatorUsernamePathCoverageTest {

    @Test
    void tcU1_nullUsername_returnsFalse() {
        assertFalse(Validator.isValidUsername(null));
    }

    @Test
    void tcU2_emptyString_returnsFalse() {
        assertFalse(Validator.isValidUsername(""));
    }

    @Test
    void tcU3_leadingSpace_returnsFalse() {
        assertFalse(Validator.isValidUsername(" John"));
    }

    @Test
    void tcU4_validUsername_returnsTrue() {
        assertTrue(Validator.isValidUsername("John Doe"));
    }

    @Test
    void tcU5_invalidCharacters_returnsFalse() {
        assertFalse(Validator.isValidUsername("John123"));
    }
}