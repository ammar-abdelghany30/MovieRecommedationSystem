import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestValidator {

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