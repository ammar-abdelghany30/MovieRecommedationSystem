package com.movie.WhiteBox.isValidUsername;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Condition Coverage tests for Validator.isValidUsername.
 *
 * Goal: every atomic condition must independently evaluate to both true and false.
 *   4 atomic conditions covered by 5 tests.
 *
 * Atomic conditions:
 *   C1 - username == null
 *   C2 - username.isEmpty()
 *   C3 - username.charAt(0) == ' '
 *   C4 - username.matches("[a-zA-Z ]+")
 *
 * Condition coverage matrix:
 *
 *   TC-U1  null        -> C1=T, C2=—
 *   TC-U2  ""          -> C1=F, C2=T
 *   TC-U3  " John"     -> C1=F, C2=F, C3=T, C4=—
 *   TC-U4  "John Doe"  -> C1=F, C2=F, C3=F, C4=T
 *   TC-U5  "John123"   -> C1=F, C2=F, C3=F, C4=F
 *
 * All 4 atomic conditions see both T and F => 100% condition coverage.
 *
 */
@DisplayName("isValidUsername - Condition Coverage")
public class ValidatorUsernameConditionCoverageTest {

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