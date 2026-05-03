package com.movie.IntegrationTest;
import com.movie.*;
import com.movie.FileReader;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests: FileReader + RecommendationEngine
 *
 * Tests the full pipeline from file parsing → recommendation generation.
 * Verifies that data parsed by FileReader flows correctly into RecommendationEngine.
 */
public class FileReaderRecommendationEngineIntegrationTest {

    private Path moviesFile;
    private Path usersFile;

    @BeforeEach
    void setUp() throws IOException {
        moviesFile = Files.createTempFile("movies", ".txt");
        usersFile  = Files.createTempFile("users",  ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(moviesFile);
        Files.deleteIfExists(usersFile);
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private void writeMovies(String content) throws IOException {
        Files.writeString(moviesFile, content);
    }

    private void writeUsers(String content) throws IOException {
        Files.writeString(usersFile, content);
    }

    // =========================================================================
    // 1. BASIC CORRECT FLOW
    // =========================================================================

    /**
     * TC-INT-01
     * Single user likes one category that matches one movie.
     * Expected: recommendation line contains that movie.
     */
    @Test
    void singleUserSingleCategoryOneMatchingMovie() throws Exception {
        writeMovies("Inception Movie,IM123\naction\n");
        writeUsers("John Doe,123456789\naction\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("For User: John Doe, 123456789"));
        assertTrue(output.contains("{action}:IM123-Inception Movie"));
    }

    /**
     * TC-INT-02
     * Single user likes one category that matches multiple movies.
     * Expected: all matching movies appear on the same category line.
     */
    @Test
    void singleUserSingleCategoryMultipleMatchingMovies() throws Exception {
        writeMovies(
                "Alien Revenge,AR123\naction\n" +
                        "Batman Returns,BR456\naction\n"
        );
        writeUsers("Jane Smith,987654321\naction\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        
    }

    /**
     * TC-INT-03
     * Single user likes two categories, each with one matching movie.
     * Expected: two separate category lines in the output.
     */
    @Test
    void singleUserMultipleCategoriesEachWithOneMatch() throws Exception {
        writeMovies(
                "Alien Revenge,AR123\naction\n" +
                        "Dark Shadows,DS456\nhorror\n"
        );
        writeUsers("Ali Hassan,123456780\naction, horror\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("{action}:"),  "Action category block must exist");
        assertTrue(output.contains("{horror}:"),  "Horror category block must exist");
        assertTrue(output.contains("AR123-Alien Revenge"), "Action movie must appear");
        assertTrue(output.contains("DS456-Dark Shadows"),  "Horror movie must appear");
    }

    /**
     * TC-INT-04
     * Multiple users, each liking different categories.
     * Expected: each user gets their own header and correct recommendations.
     */
    @Test
    void multipleUsersWithDifferentCategories() throws Exception {
        writeMovies(
                "Alien Revenge,AR123\naction\n" +
                        "Dark Shadows,DS456\nhorror\n"
        );
        writeUsers(
                "Ali Hassan,123456780\naction\n" +
                        "Sara Nour,123456781\nhorror\n"
        );

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("For User: Ali Hassan, 123456780"));
        assertTrue(output.contains("AR123-Alien Revenge"));
        assertTrue(output.contains("For User: Sara Nour, 123456781"));
        assertTrue(output.contains("DS456-Dark Shadows"));
    }

    // =========================================================================
    // 2. NO MATCH SCENARIOS
    // =========================================================================

    /**
     * TC-INT-05
     * User likes a category that no movie belongs to.
     * Expected: category line exists but with no movie entries (empty after colon).
     */
    @Test
    void userLikesCategoryWithNoMatchingMovies() throws Exception {
        writeMovies("Alien Revenge,AR123\naction\n");
        writeUsers("John Doe,123456789\nhorror\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("{horror}:"), "Horror category block must still appear");
        assertFalse(output.contains("AR123"),    "Action movie must NOT appear under horror");
    }

    /**
     * TC-INT-06
     * User likes two categories; one has matches, one does not.
     * Expected: matched category has movies, unmatched category line is empty.
     */
    @Test
    void userLikesTwoCategoriesOnlyOneHasMatches() throws Exception {
        writeMovies("Alien Revenge,AR123\naction\n");
        writeUsers("John Doe,123456789\naction, horror\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("{action}:"));
        assertTrue(output.contains("AR123-Alien Revenge"));
        assertTrue(output.contains("{horror}:"));
        // Horror line should be empty (no movie after the colon on that line)
        String horrorLine = output.lines()
                .filter(l -> l.startsWith("{horror}:"))
                .findFirst()
                .orElse("");
        assertEquals("{horror}:", horrorLine.trim(),
                "Horror line should have no movie entries");
    }

    // =========================================================================
    // 3. OUTPUT FORMAT VERIFICATION
    // =========================================================================

    /**
     * TC-INT-07
     * Verify the exact output format: "For User: username, userId"
     */
    @Test
    void outputHeaderFormatIsCorrect() throws Exception {
        writeMovies("Fast Cars,FC123\naction\n");
        writeUsers("John Doe,123456789\naction\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("For User: John Doe, 123456789"),
                "Header format must be: 'For User: <name>, <id>'");
    }

    /**
     * TC-INT-08
     * Verify the movie entry format: "movieId-movieTitle"
     */
    @Test
    void movieEntryFormatIsCorrect() throws Exception {
        writeMovies("Fast Cars,FC123\naction\n");
        writeUsers("John Doe,123456789\naction\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("FC123-Fast Cars"),
                "Movie entry format must be: '<id>-<title>'");
    }

    /**
     * TC-INT-09
     * Multiple movies in same category must be comma-separated on one line.
     */
    @Test
    void multipleMoviesInSameCategoryAreCommaSeparated() throws Exception {
        writeMovies(
                "Alien Revenge,AR123\naction\n" +
                        "Bad Boys,BB456\naction\n"
        );
        writeUsers("John Doe,123456789\naction\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        String actionLine = output.lines()
                .filter(l -> l.startsWith("{action}:"))
                .findFirst()
                .orElse("");

        assertTrue(actionLine.contains("AR123-Alien Revenge"), "First movie missing");
        assertTrue(actionLine.contains("BB456-Bad Boys"),      "Second movie missing");
        assertTrue(actionLine.contains(","),                   "Movies must be comma-separated");
    }

    // =========================================================================
    // 4. MULTI-CATEGORY MOVIES
    // =========================================================================

    /**
     * TC-INT-10
     * A movie belongs to two categories; user likes both.
     * Expected: movie appears under both category lines.
     */
    @Test
    void movieWithMultipleCategoriesAppearsUnderEachLikedCategory() throws Exception {
        writeMovies("Action Drama,AD123\naction, drama\n");
        writeUsers("John Doe,123456789\naction, drama\n");

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        String actionLine = output.lines()
                .filter(l -> l.startsWith("{action}:"))
                .findFirst().orElse("");
        String dramaLine = output.lines()
                .filter(l -> l.startsWith("{drama}:"))
                .findFirst().orElse("");

        assertTrue(actionLine.contains("AD123-Action Drama"), "Movie must appear under {action}");
        assertTrue(dramaLine.contains("AD123-Action Drama"),  "Movie must appear under {drama}");
    }

    // =========================================================================
    // 5. LARGE / STRESS
    // =========================================================================

    /**
     * TC-INT-11
     * Multiple users all liking the same category with multiple movies.
     * Expected: each user block contains all matching movies.
     */
    @Test
    void multipleUsersAllLikeSameCategoryWithMultipleMovies() throws Exception {
        writeMovies(
                "Alien Revenge,AR123\naction\n" +
                        "Bad Boys,BB456\naction\n" +
                        "Chaos City,CC789\naction\n"
        );
        writeUsers(
                "Ali Hassan,123456780\naction\n" +
                        "Sara Nour,123456781\naction\n"
        );

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        // Count user headers
        long userHeaderCount = output.lines()
                .filter(l -> l.startsWith("For User:"))
                .count();
        assertEquals(2, userHeaderCount, "Output must have 2 user headers");

        // Each user block should mention all 3 movies
        assertTrue(output.indexOf("AR123") != output.lastIndexOf("AR123"),
                "AR123 should appear for both users");
        assertTrue(output.indexOf("BB456") != output.lastIndexOf("BB456"),
                "BB456 should appear for both users");
    }

    // =========================================================================
    // 6. CASE INSENSITIVITY FOR CATEGORIES
    // =========================================================================

    /**
     * TC-INT-12
     * Movie category stored with mixed case in file; user likes lowercase version.
     * Expected: match is found (FileReader normalizes to lowercase).
     */
    @Test
    void categoryMatchingIsCaseInsensitive() throws Exception {
        writeMovies("Alien Revenge,AR123\nAction\n");   // uppercase in file
        writeUsers("John Doe,123456789\naction\n");     // lowercase in liked

        List<Movie> movies = FileReader.readMovies(moviesFile.toString());
        List<User>  users  = FileReader.readUsers(usersFile.toString());
        String output = RecommendationEngine.generateRecommendations(users, movies);

        assertTrue(output.contains("AR123-Alien Revenge"),
                "Category matching must be case-insensitive");
    }
}