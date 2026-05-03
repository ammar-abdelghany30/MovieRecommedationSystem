package com.movie.BlackBoxTesting.decision_table;
import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Decision Table — isValidUserId
 *
 * Conditions:
 *   C1: Length is exactly 9 characters
 *   C2: All characters are alphanumeric
 *   C3: First character is a digit
 *   C4: At most 1 letter present, and if so it must be at index 8 (last position)
 *
 * Rules (conditions checked in order; — = don't care / short-circuits):
 *
 * +----+-------+-------+-------+-------+-------+-------+
 * |    |  R1   |  R2   |  R3   |  R4   |  R5   |  R6   |
 * +----+-------+-------+-------+-------+-------+-------+
 * | C1 |   T   |   T   |   T   |   T   |   F   |   F   |
 * | C2 |   T   |   T   |   T   |   F   |   —   |   F   |
 * | C3 |   T   |   T   |   F   |   —   |   —   |   F   |
 * | C4 |   T   |   F   |   —   |   —   |   —   |   F   |
 * +----+-------+-------+-------+-------+-------+-------+
 * |Out | true  | false | false | false | false | false |
 * +----+-------+-------+-------+-------+-------+-------+
 *
 * Example inputs:
 *   R1: "12345678A"  → all conditions pass (1 letter at end)
 *   R1: "123456789"  → all conditions pass (0 letters, also valid)
 *   R2: "1234567AB"  → C4 fails: 2 letters present
 *   R2: "1234A6789"  → C4 fails: letter not at index 8
 *   R3: "A23456789"  → C3 fails: first char is a letter
 *   R4: "1234567#9"  → C2 fails: special character present
 *   R5: "12345"      → C1 fails: length < 9
 *   R5: "1234567890" → C1 fails: length > 9
 *   R6: null         → immediate false (null check)
 *   R6: ""           → immediate false (empty check)
 */
class ValidatorDecisionTable {

    // R1 — All conditions true, one letter at last position → valid
    @Test
    void r1_allConditionsPass_letterAtEnd_returnsTrue() {
        assertTrue(Validator.isValidUserId("12345678A"));
    }

    // R1 variant — All numeric, zero letters is also valid (≤1 letter rule)
    @Test
    void r1_allDigits_returnsTrue() {
        assertTrue(Validator.isValidUserId("123456789"));
    }

    // R2 — C4 fails: two letters present
    @Test
    void r2_twoLetters_returnsFalse() {
        assertFalse(Validator.isValidUserId("1234567AB"));
    }

    // R2 variant — C4 fails: one letter but not at index 8
    @Test
    void r2_letterNotAtLastPosition_returnsFalse() {
        assertFalse(Validator.isValidUserId("1234A6789"));
    }

    // R3 — C3 fails: first character is a letter, not a digit
    @Test
    void r3_startsWithLetter_returnsFalse() {
        assertFalse(Validator.isValidUserId("A23456789"));
    }

    // R4 — C2 fails: contains a special character
    @Test
    void r4_specialCharPresent_returnsFalse() {
        assertFalse(Validator.isValidUserId("1234567#9"));
    }

    // R5 — C1 fails: length less than 9
    @Test
    void r5_tooShort_returnsFalse() {
        assertFalse(Validator.isValidUserId("12345"));
    }

    // R5 variant — C1 fails: length greater than 9
    @Test
    void r5_tooLong_returnsFalse() {
        assertFalse(Validator.isValidUserId("1234567890"));
    }

    // R6 — null input → immediate false
    @Test
    void r6_null_returnsFalse() {
        assertFalse(Validator.isValidUserId(null));
    }

    // R6 variant — empty string → immediate false
    @Test
    void r6_emptyString_returnsFalse() {
        assertFalse(Validator.isValidUserId(""));
    }
    /**
     * Decision Table — Full Movie ID Validation
     * Combines: isValidMovieIdLetters(title, id) + isValidMovieIdNumbers(id)
     *
     * Title used in all tests: "The Dark Knight" → expected letters prefix = "TDK"
     *
     * Conditions:
     *   C1: Letters portion of ID matches capital first-letters of each title word
     *   C2: Exactly 3 digits present in the ID
     *   C3: All 3 digits are unique (don't care if C2 is false — never reached)
     *
     * +----+-------+-------+-------+-------+-------+
     * |    |  R1   |  R2   |  R3   |  R4   |  R5   |
     * +----+-------+-------+-------+-------+-------+
     * | C1 |   T   |   T   |   T   |   F   |   F   |
     * | C2 |   T   |   T   |   F   |   T   |   F   |
     * | C3 |   T   |   F   |   —   |   F   |   —   |
     * +----+-------+-------+-------+-------+-------+
     * |Out | true  | false | false | false | false |
     * +----+-------+-------+-------+-------+-------+
     *
     * Example inputs:
     *   R1: "TDK123" → all conditions pass                          → true
     *   R2: "TDK112" → C3 fails: digit '1' repeats                 → false
     *   R3: "TDK12"  → C2 fails: only 2 digits                     → false
     *   R4: "TD123"  → C1 fails: missing 'K', digits fine          → false
     *   R5: "TD12"   → C1 fails: wrong letters, C2 fails too       → false
     */

        static final String TITLE = "The Dark Knight";

        // R1 — All conditions pass: correct letters, 3 digits, all unique
        @Test
        void r1_allConditionsPass_returnsTrue() {
            assertTrue(Validator.isValidMovieIdLetters(TITLE, "TDK123"));
            assertTrue(Validator.isValidMovieIdNumbers("TDK123"));
        }

        // R1 variant — digits in different order, still unique
        @Test
        void r1_differentUniqueDigits_returnsTrue() {
            assertTrue(Validator.isValidMovieIdLetters(TITLE, "TDK987"));
            assertTrue(Validator.isValidMovieIdNumbers("TDK987"));
        }

        // R2 — C3 fails: digits present but not all unique (1 repeats)
        @Test
        void r2_duplicateDigit_returnsFalse() {
            assertTrue(Validator.isValidMovieIdLetters(TITLE, "TDK112"));
            assertFalse(Validator.isValidMovieIdNumbers("TDK112"));
        }

        // R2 variant — all three digits the same
        @Test
        void r2_allDigitsSame_returnsFalse() {
            assertTrue(Validator.isValidMovieIdLetters(TITLE, "TDK111"));
            assertFalse(Validator.isValidMovieIdNumbers("TDK111"));
        }

        // R3 — C2 fails: only 2 digits present (C3 never reached)
        @Test
        void r3_onlyTwoDigits_returnsFalse() {
            assertTrue(Validator.isValidMovieIdLetters(TITLE, "TDK12"));
            assertFalse(Validator.isValidMovieIdNumbers("TDK12"));
        }

        // R3 variant — no digits at all
        @Test
        void r3_noDigits_returnsFalse() {
            assertTrue(Validator.isValidMovieIdLetters(TITLE, "TDK"));
            assertFalse(Validator.isValidMovieIdNumbers("TDK"));
        }

        // R4 — C1 fails: wrong letters prefix, digits are valid
        @Test
        void r4_wrongLetters_correctDigits_returnsFalse() {
            assertFalse(Validator.isValidMovieIdLetters(TITLE, "TD123"));
        }

        // R4 variant — extra letter added to prefix
        @Test
        void r4_extraLetter_returnsFalse() {
            assertFalse(Validator.isValidMovieIdLetters(TITLE, "TDKX123"));
        }

        // R5 — C1 and C2 both fail: wrong letters AND too few digits
        @Test
        void r5_wrongLettersAndTooFewDigits_returnsFalse() {
            assertFalse(Validator.isValidMovieIdLetters(TITLE, "TD12"));
            assertFalse(Validator.isValidMovieIdNumbers("TD12"));
        }
    }