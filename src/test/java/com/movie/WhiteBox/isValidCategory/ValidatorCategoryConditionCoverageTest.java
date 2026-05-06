package com.movie.WhiteBox.isValidCategory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Condition Coverage tests for Validator.isValidCategory.
 */
@DisplayName("isValidCategory - Condition Coverage")
public class ValidatorCategoryConditionCoverageTest {

    @Test
    @DisplayName("Condition: ALLOWED_CATEGORIES.contains(...) is True")
    void tcC1_conditionTrue() {
        assertTrue(Validator.isValidCategory("sci-fi"));
    }

    @Test
    @DisplayName("Condition: ALLOWED_CATEGORIES.contains(...) is False")
    void tcC2_conditionFalse() {
        assertFalse(Validator.isValidCategory("mystery"));
    }
}
