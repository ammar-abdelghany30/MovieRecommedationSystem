package com.movie.WhiteBox.isValidCategory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.movie.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Path Coverage tests for Validator.isValidCategory.
 */
@DisplayName("isValidCategory - Path Coverage")
public class ValidatorCategoryPathCoverageTest {

    @Test
    @DisplayName("Path 1: M1-M2 (Result True)")
    void path1() {
        assertTrue(Validator.isValidCategory("drama"));
    }

    @Test
    @DisplayName("Path 2: M1-M2 (Result False)")
    void path2() {
        assertFalse(Validator.isValidCategory("war"));
    }
}
