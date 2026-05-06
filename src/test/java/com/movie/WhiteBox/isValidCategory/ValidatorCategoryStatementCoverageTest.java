package com.movie.WhiteBox.isValidCategory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Statement Coverage tests for Validator.isValidCategory.
 */
@DisplayName("isValidCategory - Statement Coverage")
public class ValidatorCategoryStatementCoverageTest {

    @Test
    @DisplayName("TC-S1: valid category hits M2 (True)")
    void tcS1_validCategory_returnsTrue() {
        assertTrue(Validator.isValidCategory("horror"));
    }

    @Test
    @DisplayName("TC-S2: invalid category hits M2 (False)")
    void tcS2_invalidCategory_returnsFalse() {
        assertFalse(Validator.isValidCategory("documentary"));
    }
}
