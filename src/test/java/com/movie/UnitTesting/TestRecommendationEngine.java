package com.movie.UnitTesting;

import com.movie.Movie;
import com.movie.User;
import com.movie.RecommendationEngine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

public class TestRecommendationEngine {

    // ── HELPERS ─────────────────────────────────────────────────
    private Movie movie(String title, String id, String... cats) {
        return new Movie(title, id, Arrays.asList(cats));
    }
    private User user(String name, String id, String... cats) {
        return new User(name, id, Arrays.asList(cats));
    }

    // =========================================================
    // SCENARIO 1 — Basic Matching
    // =========================================================

    // TC-RE01: Single user, single movie, matching category
    @Test
    void singleUserSingleMovieMatch() {
        // Arrange
        List<Movie> movies = List.of(movie("The Matrix", "TM123", "action"));
        List<User> users  = List.of(user("John Smith", "12345678A", "action"));
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("For User: John Smith, 12345678A"));
        assertTrue(result.contains("{action}:"));
        assertTrue(result.contains("TM123-The Matrix"));
    }

    // TC-RE02: User likes category with no matching movie
    @Test
    void userLikesCategoryWithNoMatch() {
        // Arrange
        List<Movie> movies = List.of(movie("The Matrix", "TM123", "action"));
        List<User> users  = List.of(user("John Smith", "12345678A", "drama"));
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("{drama}:"));
        assertFalse(result.contains("TM123"));
    }

    // TC-RE03: User likes multiple categories
    @Test
    void userLikesMultipleCategories() {
        // Arrange
        List<Movie> movies = List.of(
                movie("The Matrix", "TM123", "action"),
                movie("Inception", "I456", "drama")
        );
        List<User> users = List.of(
                user("John Smith", "12345678A", "action", "drama")
        );
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("{action}:"));
        assertTrue(result.contains("{drama}:"));
        assertTrue(result.contains("TM123-The Matrix"));
        assertTrue(result.contains("I456-Inception"));
    }

    // TC-RE04: Multiple movies match same category
    @Test
    void multipleMoviesMatchSameCategory() {
        // Arrange
        List<Movie> movies = List.of(
                movie("The Matrix", "TM123", "action"),
                movie("John Wick", "JW456", "action")
        );
        List<User> users = List.of(user("John Smith", "12345678A", "action"));
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("TM123-The Matrix"));
        assertTrue(result.contains("JW456-John Wick"));
    }

    // =========================================================
    // SCENARIO 2 — Multiple Users
    // =========================================================

    // TC-RE05: Two users each get their own block
    @Test
    void twoUsersGetSeparateBlocks() {
        // Arrange
        List<Movie> movies = List.of(
                movie("The Matrix", "TM123", "action"),
                movie("Inception", "I456", "drama")
        );
        List<User> users = List.of(
                user("John Smith", "12345678A", "action"),
                user("Jane Doe",   "987654321", "drama")
        );
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("For User: John Smith, 12345678A"));
        assertTrue(result.contains("For User: Jane Doe, 987654321"));
    }

    // TC-RE06: Each user only sees their own category movies
    @Test
    void eachUserSeesOnlyTheirCategoryMovies() {
        // Arrange
        List<Movie> movies = List.of(
                movie("The Matrix", "TM123", "action"),
                movie("Inception",  "I456",  "drama")
        );
        List<User> users = List.of(
                user("John Smith", "12345678A", "action"),
                user("Jane Doe",   "987654321", "drama")
        );
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert — split by user blocks
        String[] blocks = result.split("For User:");
        // John's block should NOT contain Inception
        assertFalse(blocks[1].contains("I456-Inception"));
        // Jane's block should NOT contain The Matrix
        assertFalse(blocks[2].contains("TM123-The Matrix"));
    }

    // =========================================================
    // SCENARIO 3 — Output Format Exactness
    // =========================================================

    // TC-RE07: Header line format exactly matches requirements
    @Test
    void headerLineFormatIsCorrect() {
        // Arrange
        List<Movie> movies = List.of(movie("The Matrix", "TM123", "action"));
        List<User>  users  = List.of(user("John Smith", "12345678A", "action"));
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert — exact format from requirements doc
        assertTrue(result.contains("For User: John Smith, 12345678A\n"));
    }

    // TC-RE08: Category line format — {category}: id-title
    @Test
    void categoryLineFormatIsCorrect() {
        // Arrange
        List<Movie> movies = List.of(movie("The Matrix", "TM123", "action"));
        List<User>  users  = List.of(user("John Smith", "12345678A", "action"));
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("{action}:TM123-The Matrix"));
    }

    // TC-RE09: Multiple movies in category separated by comma
    @Test
    void multipleMoviesSeparatedByComma() {
        // Arrange
        List<Movie> movies = List.of(
                movie("The Matrix", "TM123", "action"),
                movie("John Wick",  "JW456", "action")
        );
        List<User> users = List.of(user("John Smith", "12345678A", "action"));
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("TM123-The Matrix, JW456-John Wick"));
    }

    // =========================================================
    // SCENARIO 4 — Edge Cases
    // =========================================================

    // TC-RE10: Empty movies list — user block generated but categories empty
    @Test
    void emptyMoviesList() {
        // Arrange
        List<Movie> movies = List.of();
        List<User>  users  = List.of(user("John Smith", "12345678A", "action"));
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("For User: John Smith, 12345678A"));
        assertTrue(result.contains("{action}:"));
        assertFalse(result.contains("TM123"));
    }

    // TC-RE11: Empty users list — empty output
    @Test
    void emptyUsersList() {
        // Arrange
        List<Movie> movies = List.of(movie("The Matrix", "TM123", "action"));
        List<User>  users  = List.of();
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.isEmpty());
    }

    // TC-RE12: Both lists empty — empty output
    @Test
    void bothListsEmpty() {
        // Arrange
        List<Movie> movies = List.of();
        List<User>  users  = List.of();
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.isEmpty());
    }

    // TC-RE13: Movie belongs to multiple categories — appears in both
    @Test
    void movieInMultipleCategoriesAppearsInBoth() {
        // Arrange
        List<Movie> movies = List.of(
                movie("The Dark Knight", "TDK123", "action", "thriller")
        );
        List<User> users = List.of(
                user("John Smith", "12345678A", "action", "thriller")
        );
        // Act
        String result = RecommendationEngine.generateRecommendations(users, movies);
        // Assert
        assertTrue(result.contains("{action}:TDK123-The Dark Knight"));
        assertTrue(result.contains("{thriller}:TDK123-The Dark Knight"));
    }
}