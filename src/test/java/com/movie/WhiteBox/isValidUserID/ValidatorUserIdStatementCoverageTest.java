package com.movie.WhiteBox.isValidUserID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Statement Coverage tests for Validator.isValidUserId.
 * Goal: execute every statement in isValidUserId at least once.
 *   12 statements (M2..M13) hit by 6 tests. (The CFG is in the report)
 * Coverage matrix (which test hits which CFG node):
 *   TC-I1  null          -> M2, M3
 *   TC-I2  "12345678@"   -> M2, M4, M5
 *   TC-I3  "a12345678"   -> M2, M4, M6, M7
 *   TC-I4  "1234567ab"   -> M2, M4, M6, M8, M9, M10
 *   TC-I5  "1234a5678"   -> M2, M4, M6, M8, M9, M11, M12
 *   TC-I6  "123456789"   -> M2, M4, M6, M8, M9, M11, M13
 * Union of all tests: {M2..M13}  =>  100% statement coverage.
 */
@DisplayName("isValidUserId - Statement Coverage")
public class ValidatorUserIdStatementCoverageTest {

    @Test
    @DisplayName("TC-I1: null userId hits M2 -> M3")
    void tcI1_nullUserId_returnsFalse() {
        // Covers: M2 (true via short-circuit), M3 (return false)
        assertFalse(Validator.isValidUserId(null));
    }

    @Test
    @DisplayName("TC-I2: special character hits M2 -> M4 -> M5")
    void tcI2_nonAlphanumeric_returnsFalse() {
        // Covers: M2 (false), M4 (true - regex fails), M5 (return false)
        // Length is 9, but '@' is not alphanumeric.
        assertFalse(Validator.isValidUserId("12345678@"));
    }

    @Test
    @DisplayName("TC-I3: starts with letter hits M2 -> M4 -> M6 -> M7")
    void tcI3_startsWithLetter_returnsFalse() {
        // Covers: M2 (false), M4 (false), M6 (true), M7 (return false)
        assertFalse(Validator.isValidUserId("a12345678"));
    }

    @Test
    @DisplayName("TC-I4: two letters hit M2 -> M4 -> M6 -> M8 -> M9 -> M10")
    void tcI4_tooManyLetters_returnsFalse() {
        // Covers: M2 (false), M4 (false), M6 (false),
        //         M8 (compute letterCount = 2),
        //         M9 (true - letterCount > 1), M10 (return false)
        assertFalse(Validator.isValidUserId("1234567ab"));
    }

    @Test
    @DisplayName("TC-I5: letter not at end hits M2..M9 -> M11 -> M12")
    void tcI5_letterNotAtEnd_returnsFalse() {
        // Covers: M2 (false), M4 (false), M6 (false),
        //         M8 (letterCount = 1),
        //         M9 (false), M11 (true - letter exists but not at index 8),
        //         M12 (return false)
        assertFalse(Validator.isValidUserId("1234a5678"));
    }

    @Test
    @DisplayName("TC-I6: all digits hit M2..M9 -> M11 -> M13")
    void tcI6_allDigits_returnsTrue() {
        // Covers: M2 (false), M4 (false), M6 (false),
        //         M8 (letterCount = 0),
        //         M9 (false), M11 (false - short-circuit on letterCount==1),
        //         M13 (return true)
        assertTrue(Validator.isValidUserId("123456789"));
    }
}