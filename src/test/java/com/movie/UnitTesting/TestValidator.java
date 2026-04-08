package com.movie.UnitTesting;

import com.movie.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestValidator {

    // Scenario 1 -> Testing that each Movie word in title starts with Captial letter
    @Test
    public void testValidMovieTitle_AllWordsCapitalized_ReturnsTrue() {
        // Arrange
        String title = "The Dark Knight";

        // Act
        boolean result = Validator.isValidMovieTitle(title);

        // Assert
        assertTrue(result, "Title with all capitalized words should be valid");
    }
    @Test
    public void testValidMovieTitle_AllWordsCapitalized_ReturnsTrue2() {
        // Arrange
        String title = "La Trag3 Wala Esteslam";

        // Act
        boolean result = Validator.isValidMovieTitle(title);

        // Assert
        assertTrue(result, "Title with all capitalized words should be valid");
    }

    // Scenario 2 -> Errors in Title
    @Test
    public void testValidMovieTitle_EmptyString_ReturnsFalse() {
        // Arrange
        String title = "";

        // Act
        boolean result = Validator.isValidMovieTitle(title);

        // Assert
        assertFalse(result, "Empty title should be invalid");
    }
    @Test
    public void testValidMovieTitle_NullTitle_ReturnsFalse() {
        // Arrange
        String title = null;

        // Act
        boolean result = Validator.isValidMovieTitle(title);

        // Assert
        assertFalse(result, "Null title should be invalid");
    }
    @Test
    public void testValidMovieTitle_NotCapitalized_ReturnsFalse()
    {
        String title = "the clown";
        boolean result = Validator.isValidMovieTitle(title);
        assertFalse(result,"All Title words should be Capital");
    }
    @Test
    public void testValidMovieTitle_NotCapitalized_ReturnsFalse2()
    {
        String title = "the Clown";
        boolean result = Validator.isValidMovieTitle(title);
        assertFalse(result,"All Title words should be Capital");
    }
    @Test
    public void testValidMovieTitle_NotCapitalized_ReturnsFalse3()
    {
        String title = "The clown";
        boolean result = Validator.isValidMovieTitle(title);
        assertFalse(result,"All Title words should be Capital");
    }

}