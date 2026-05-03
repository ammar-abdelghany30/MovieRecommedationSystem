package com.movie.WhiteBox.isValidUsername;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Statement Coverage tests for Validator.isValidUsername.
 * Goal: execute every statement in isValidUsername at least once.
 *   7 statements (N2..N8) hit by 4 tests.
 *
 *
 * Coverage matrix (which test hits which CFG node):
 *
 *   TC-U1  null         -> N2, N3
 *   TC-U2  " John"      -> N2, N4, N5
 *   TC-U3  "John Doe"   -> N2, N4, N6, N7
 *   TC-U4  "John123"    -> N2, N4, N6, N8
 *
 * Union of all tests: {N2, N3, N4, N5, N6, N7, N8}  =>  100% statement coverage.
 */
public class ValidatorUsernameStatementCoverageTest {

    @Test
    @DisplayName("TC-U1: null username hits N2 -> N3")
    void tcU1_nullUsername_returnsFalse() {
        // Covers: N2 (decision evaluates true via short-circuit), N3 (return false)
        assertFalse(Validator.isValidUsername(null));
    }

    @Test
    @DisplayName("TC-U2: leading space hits N2 -> N4 -> N5")
    void tcU2_leadingSpace_returnsFalse() {
        // Covers: N2 (false), N4 (true), N5 (return false)
        assertFalse(Validator.isValidUsername(" John"));
    }

    @Test
    @DisplayName("TC-U3: valid username hits N2 -> N4 -> N6 -> N7")
    void tcU3_validUsername_returnsTrue() {
        // Covers: N2 (false), N4 (false), N6 (true), N7 (return true)
        assertTrue(Validator.isValidUsername("John Doe"));
    }

    @Test
    @DisplayName("TC-U4: invalid characters hit N2 -> N4 -> N6 -> N8")
    void tcU4_invalidCharacters_returnsFalse() {
        // Covers: N2 (false), N4 (false), N6 (false), N8 (return false)
        assertFalse(Validator.isValidUsername("John123"));
    }
}