package com.movie.BlackBoxTesting.BVA;

import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================================
 *              BOUNDARY VALUE ANALYSIS — Movie Validators
 * ============================================================================
 *
 *  isValidMovieIdNumbers(id)
 *  -------------------------
 *   Rule: ID must contain exactly 3 digits, all unique.
 *
 *   Digit-count boundaries (target = 3):
 *     0 digits  -> false   (well below)
 *     1 digit   -> false
 *     2 digits  -> false   (boundary - 1)
 *     3 digits  -> true    (boundary, valid)
 *     4 digits  -> false   (boundary + 1)
 *     5 digits  -> false   (well above)
 *
 *   Uniqueness boundaries (3 digits present):
 *     0 unique (all same, "111")    -> false
 *     2 unique (one duplicate)      -> false
 *     3 unique (all distinct)       -> true
 *
 *
 *  isValidMovieIdLetters(title, id)
 *  --------------------------------
 *   Rule: ID's letter prefix must equal the first letter of each word
 *   in the title — i.e. letter count must equal word count.
 *
 *   1-word title "Matrix" (expects 1 letter):
 *     0 letters in id  -> false (boundary - 1)
 *     1 letter correct -> true  (boundary)
 *     2 letters in id  -> false (boundary + 1)
 *
 *   2-word title "The Matrix" (expects 2 letters):
 *     1 letter   -> false (boundary - 1)
 *     2 letters  -> true  (boundary)
 *     3 letters  -> false (boundary + 1)
 *
 *
 *  isValidMovieTitle(title)
 *  ------------------------
 *   Length boundaries:
 *     length 0  ("")   -> false
 *     length 1  ("A")  -> true   (smallest valid)
 *     length 1  ("a")  -> false  (case boundary)
 *
 *   Word-count / case boundaries (each word must start uppercase):
 *     1 word, first letter 'A' (lower ASCII boundary of upper case) -> true
 *     1 word, first letter 'Z' (upper ASCII boundary)               -> true
 *     2 words, both uppercase first letters                         -> true
 *
 *
 *  isValidCategory(category)
 *  -------------------------
 *   Length / whitespace boundaries:
 *     ""           -> false
 *     "action"     -> true   (exact, no padding)
 *     " action "   -> true   (1 leading + 1 trailing space — trimmed)
 *     "  action  " -> true   (multiple surrounding spaces)
 * ============================================================================
 */
public class TestMovieBVA {

    // ========== isValidMovieIdNumbers — digit count boundaries ==========

    @Test
    void testIdNumbers_ZeroDigits() {
        assertFalse(Validator.isValidMovieIdNumbers("AB"));
    }

    @Test
    void testIdNumbers_OneDigit() {
        assertFalse(Validator.isValidMovieIdNumbers("AB1"));
    }

    @Test
    void testIdNumbers_TwoDigits_BelowBoundary() {
        assertFalse(Validator.isValidMovieIdNumbers("AB12"));
    }

    @Test
    void testIdNumbers_ThreeDigits_OnBoundary() {
        assertTrue(Validator.isValidMovieIdNumbers("AB123"));
    }

    @Test
    void testIdNumbers_FourDigits_AboveBoundary() {
        assertFalse(Validator.isValidMovieIdNumbers("AB1234"));
    }

    @Test
    void testIdNumbers_FiveDigits() {
        assertFalse(Validator.isValidMovieIdNumbers("AB12345"));
    }

    // ========== isValidMovieIdNumbers — uniqueness boundaries ==========

    @Test
    void testIdNumbers_AllThreeSame_ZeroUnique() {
        assertFalse(Validator.isValidMovieIdNumbers("AB111"));
    }

    @Test
    void testIdNumbers_OneDuplicate_TwoUnique() {
        assertFalse(Validator.isValidMovieIdNumbers("AB112"));
    }

    @Test
    void testIdNumbers_AllUnique_ThreeUnique() {
        assertTrue(Validator.isValidMovieIdNumbers("AB123"));
    }

    // ========== isValidMovieIdLetters — 1-word title boundaries ==========

    @Test
    void testIdLetters_OneWordTitle_ZeroLetters_BelowBoundary() {
        assertFalse(Validator.isValidMovieIdLetters("Matrix", "123"));
    }

    @Test
    void testIdLetters_OneWordTitle_OneLetter_OnBoundary() {
        assertTrue(Validator.isValidMovieIdLetters("Matrix", "M123"));
    }

    @Test
    void testIdLetters_OneWordTitle_TwoLetters_AboveBoundary() {
        assertFalse(Validator.isValidMovieIdLetters("Matrix", "MA123"));
    }

    // ========== isValidMovieIdLetters — 2-word title boundaries ==========

    @Test
    void testIdLetters_TwoWordTitle_OneLetter_BelowBoundary() {
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "T123"));
    }

    @Test
    void testIdLetters_TwoWordTitle_TwoLetters_OnBoundary() {
        assertTrue(Validator.isValidMovieIdLetters("The Matrix", "TM123"));
    }

    @Test
    void testIdLetters_TwoWordTitle_ThreeLetters_AboveBoundary() {
        assertFalse(Validator.isValidMovieIdLetters("The Matrix", "TMA123"));
    }

    // ========== isValidMovieTitle — length boundaries ==========

    @Test
    void testTitle_LengthZero_BelowBoundary() {
        assertFalse(Validator.isValidMovieTitle(""));
    }

    @Test
    void testTitle_LengthOne_Uppercase_OnBoundary() {
        assertTrue(Validator.isValidMovieTitle("A"));
    }

    @Test
    void testTitle_LengthOne_Lowercase_CaseBoundary() {
        assertFalse(Validator.isValidMovieTitle("a"));
    }

    // ========== isValidMovieTitle — case range boundaries ==========

    @Test
    void testTitle_FirstLetter_A_LowerUppercaseBoundary() {
        assertTrue(Validator.isValidMovieTitle("Apple"));
    }

    @Test
    void testTitle_FirstLetter_Z_UpperUppercaseBoundary() {
        assertTrue(Validator.isValidMovieTitle("Zebra"));
    }

    @Test
    void testTitle_TwoWords_BothUppercaseFirst() {
        assertTrue(Validator.isValidMovieTitle("The Matrix"));
    }

    // ========== isValidCategory — whitespace boundaries ==========

    @Test
    void testCategory_Empty_BelowBoundary() {
        assertFalse(Validator.isValidCategory(""));
    }

    @Test
    void testCategory_NoPadding_OnBoundary() {
        assertTrue(Validator.isValidCategory("action"));
    }

    @Test
    void testCategory_OneSpaceEachSide() {
        assertTrue(Validator.isValidCategory(" action "));
    }

    @Test
    void testCategory_MultipleSurroundingSpaces() {
        assertTrue(Validator.isValidCategory("  action  "));
    }
}