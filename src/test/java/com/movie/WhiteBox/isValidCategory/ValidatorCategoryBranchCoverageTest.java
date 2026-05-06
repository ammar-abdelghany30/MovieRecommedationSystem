package com.movie.WhiteBox.isValidCategory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Branch Coverage tests for Validator.isValidCategory.
 */
@DisplayName("isValidCategory - Branch Coverage")
public class ValidatorCategoryBranchCoverageTest {

    @Test
    @DisplayName("TC-B1: contains returns True")
    void tcB1_trueBranch() {
        assertTrue(Validator.isValidCategory("ACTION "));
    }

    @Test
    @DisplayName("TC-B2: contains returns False")
    void tcB2_falseBranch() {
        assertFalse(Validator.isValidCategory("western"));
    }
}
