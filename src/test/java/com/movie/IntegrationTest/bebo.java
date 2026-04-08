package com.movie.IntegrationTest;

import com.movie.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class bebo {
    @Test
    public void testValidMovieTitle_AllWordsCapitalized_ReturnsTrue() {
        // Arrange
        String title = "The Dark Knight";

        // Act
        boolean result = Validator.isValidMovieTitle(title);

        // Assert
        assertTrue(result, "Title with all capitalized words should be valid");
    }
}
