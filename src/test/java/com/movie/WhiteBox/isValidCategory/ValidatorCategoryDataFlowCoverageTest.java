package com.movie.WhiteBox.isValidCategory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Data Flow Coverage tests for Validator.isValidCategory.
 * Variable: category
 *   Def: Input parameter
 *   Use: M2 (category.trim().toLowerCase())
 */
@DisplayName("isValidCategory - Data Flow Coverage")
public class ValidatorCategoryDataFlowCoverageTest {

    @Test
    @DisplayName("DU Pair (category, Entry, M2)")
    void testCategoryDefToUse() {
        assertTrue(Validator.isValidCategory(" comedy "));
    }
}
