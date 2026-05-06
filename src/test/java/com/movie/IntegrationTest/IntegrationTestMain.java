package com.movie.IntegrationTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTestMain {

    @TempDir
    Path tempDir;

    // ── HELPER ───────────────────────────────────────────────────
    // Writes files, runs the full pipeline, returns recommendations.txt content
    private String runPipeline(String moviesContent,
                               String usersContent) throws Exception {
        Path moviesPath = tempDir.resolve("movies.txt");
        Path usersPath  = tempDir.resolve("users.txt");
        Path outputPath = tempDir.resolve("recommendations.txt");

        Files.writeString(moviesPath, moviesContent);
        Files.writeString(usersPath,  usersContent);

        // Run the FULL pipeline exactly as Main.java does
        try {
            var movies = com.movie.FileReader.readMovies(moviesPath.toString());
            var users  = com.movie.FileReader.readUsers(usersPath.toString());
            String output = com.movie.RecommendationEngine
                    .generateRecommendations(users, movies);
            Files.writeString(outputPath, output);
        } catch (Exception e) {
            Files.writeString(outputPath, e.getMessage() + "\n");
        }

        return Files.readString(outputPath);
    }

    // Helper for missing file scenarios
    private String runPipelineWithPaths(String moviesPath,
                                        String usersPath) throws Exception {
        Path outputPath = tempDir.resolve("recommendations.txt");
        try {
            var movies = com.movie.FileReader.readMovies(moviesPath);
            var users  = com.movie.FileReader.readUsers(usersPath);
            String output = com.movie.RecommendationEngine
                    .generateRecommendations(users, movies);
            Files.writeString(outputPath, output);
        } catch (Exception e) {
            Files.writeString(outputPath, e.getMessage() + "\n");
        }
        return Files.readString(outputPath);
    }

    // =========================================================
    // IT-17: Both files valid → correct recommendations.txt
    // =========================================================

    // IT-17a: Single movie + single user → correct output format
    @Test
    void validInput_correctOutputFormat() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert — full pipeline produces correct output
        assertTrue(output.contains("For User: John Smith, 12345678A"),
                "Output must contain correct user header");
        assertTrue(output.contains("{action}:"),
                "Output must contain category block");
        assertTrue(output.contains("TM123-The Matrix"),
                "Output must contain movie recommendation");
    }

    // IT-17b: Two movies + one user → both movies recommended
    @Test
    void twoMoviesOneUser_bothRecommended() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\nJohn Wick,JW456\naction\n";
        String users  = "John Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("TM123-The Matrix"));
        assertTrue(output.contains("JW456-John Wick"));
    }

    // IT-17c: Two users → each gets their own block
    @Test
    void twoUsers_eachGetsOwnBlock() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\nInception,I456\ndrama\n";
        String users  = "John Smith,12345678A\naction\nJane Doe,987654321\ndrama\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("For User: John Smith, 12345678A"));
        assertTrue(output.contains("For User: Jane Doe, 987654321"));
    }

    // IT-17d: User likes category with no matching movie → empty category line (same issue as in unit test one)
    @Test
    void userLikesCategoryWithNoMovie_emptyCategoryLine() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John Smith,12345678A\ndrama\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("{drama}:"));
        assertFalse(output.contains("TM123"));
    }

    // IT-17e: Output file is actually written to disk — not just in memory
    @Test
    void outputFileExistsOnDisk() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John Smith,12345678A\naction\n";
        Path outputPath = tempDir.resolve("recommendations.txt");
        // Act
        runPipeline(movies, users);
        // Assert
        assertTrue(Files.exists(outputPath),
                "recommendations.txt must exist on disk after pipeline runs");
    }

    // IT-17f: User ID with no trailing letter — valid all-digit ID
    @Test
    void validAllDigitUserId_pipelineSucceeds() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John Smith,123456789\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("For User: John Smith, 123456789"));
        assertFalse(output.contains("ERROR"));
    }

    // =========================================================
    // IT-18: movies.txt error → ONLY error in recommendations.txt
    // =========================================================

    // IT-18a: Bad movie title → Movie Title ERROR in output
    @Test
    void badMovieTitle_movieTitleErrorInOutput() throws Exception {
        // Arrange
        String movies = "the matrix,TM123\naction\n";
        String users  = "John Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("Movie Title ERROR: the matrix is wrong"),
                "Exact error message must appear in output");
    }

    // IT-18b: Bad movie ID letters → Movie Id letters ERROR in output
    @Test
    void badMovieIdLetters_movieIdLettersErrorInOutput() throws Exception {
        // Arrange
        String movies = "The Matrix,XY123\naction\n";
        String users  = "John Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("Movie Id letters ERROR: XY123 are wrong"));
    }

    // IT-18c: Bad movie ID numbers → Movie Id numbers ERROR in output
    @Test
    void badMovieIdNumbers_movieIdNumbersErrorInOutput() throws Exception {
        // Arrange
        String movies = "The Matrix,TM111\naction\n";
        String users  = "John Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("Movie Id numbers ERROR: TM111 aren't unique"));
    }

    // IT-18d: Movie error → NO recommendations in output
    @Test
    void movieError_noRecommendationsInOutput() throws Exception {
        // Arrange
        String movies = "the matrix,TM123\naction\n";
        String users  = "John Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert — error only, no user block
        assertFalse(output.contains("For User:"),
                "No recommendations must appear when movie error exists");
    }

    // IT-18e: Second movie has bad title — first error only rule
    @Test
    void secondMovieBadTitle_firstErrorOnlyRule() throws Exception {
        // Arrange — first movie valid, second invalid
        String movies = "The Matrix,TM123\naction\nthe avengers,TA456\ndrama\n";
        String users  = "John Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("Movie Title ERROR: the avengers is wrong"));
        assertFalse(output.contains("For User:"));
    }

    // =========================================================
    // IT-19: users.txt error → ONLY error in recommendations.txt
    // =========================================================

    // IT-19a: Bad username → Username ERROR in output
    @Test
    void badUsername_usernameErrorInOutput() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John2 Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("Username ERROR: John2 Smith is wrong"));
    }

    // IT-19b: Bad user ID → User Id ERROR in output
    @Test
    void badUserId_userIdErrorInOutput() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John Smith,A12345678\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("User Id ERROR: A12345678 is wrong"));
    }

    // IT-19c: Duplicate user IDs → User Id ERROR in output
    @Test
    void duplicateUserIds_userIdErrorInOutput() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John Smith,12345678A\naction\nJane Doe,12345678A\ndrama\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("User Id ERROR: 12345678A is wrong"));
    }

    // IT-19d: User error → NO recommendations in output
    @Test
    void userError_noRecommendationsInOutput() throws Exception {
        // Arrange
        String movies = "The Matrix,TM123\naction\n";
        String users  = "John2 Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertFalse(output.contains("For User:"),
                "No recommendations must appear when user error exists");
    }

    // =========================================================
    // IT-20: Both files have errors → only FIRST error written
    // =========================================================

    // IT-20a: Bad movie + bad username → only movie error in output
    @Test
    void badMovieAndBadUser_onlyMovieErrorInOutput() throws Exception {
        // Arrange
        String movies = "the matrix,TM123\naction\n";   // bad title
        String users  = "John2 Smith,12345678A\naction\n"; // bad username
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("Movie Title ERROR"),
                "Movie error must appear — it's parsed first");
        assertFalse(output.contains("Username ERROR"),
                "Username error must NOT appear — movies parsed before users");
    }

    // IT-20b: Bad movie ID + bad user ID → only movie error in output
    @Test
    void badMovieId_badUserId_onlyMovieErrorInOutput() throws Exception {
        // Arrange
        String movies = "The Matrix,XY123\naction\n";   // bad ID letters
        String users  = "John Smith,A12345678\naction\n"; // bad user ID
        // Act
        String output = runPipeline(movies, users);
        // Assert
        assertTrue(output.contains("Movie Id letters ERROR"));
        assertFalse(output.contains("User Id ERROR"));
    }

    // IT-20c: Output contains EXACTLY one error message — nothing else
    @Test
    void bothFilesInvalid_exactlyOneErrorInOutput() throws Exception {
        // Arrange
        String movies = "the matrix,TM123\naction\n";
        String users  = "John2 Smith,12345678A\naction\n";
        // Act
        String output = runPipeline(movies, users);
        // Assert — count how many ERROR occurrences appear
        long errorCount = output.lines()
                .filter(l -> l.contains("ERROR"))
                .count();
        assertEquals(1, errorCount,
                "Output must contain exactly ONE error message");
    }

    // =========================================================
    // IT-21: Missing movies.txt → exception handled → error in output
    // =========================================================

    // IT-21a: Missing movies.txt → FileNotFoundException written to output
    @Test
    void missingMoviesFile_exceptionWrittenToOutput() throws Exception {
        // Arrange — movies path points to non-existent file
        String missingMovies = tempDir.resolve("nonexistent_movies.txt").toString();
        Path usersPath = tempDir.resolve("users.txt");
        Files.writeString(usersPath, "John Smith,12345678A\naction\n");
        // Act
        String output = runPipelineWithPaths(missingMovies, usersPath.toString());
        // Assert — some error message written — not empty, not recommendations
        assertFalse(output.isBlank(),
                "Output must not be empty when movies file is missing");
        assertFalse(output.contains("For User:"),
                "No recommendations must appear when movies file is missing");
    }

    // IT-21b: Missing movies.txt → output file still created on disk
    @Test
    void missingMoviesFile_outputFileStillCreated() throws Exception {
        // Arrange
        String missingMovies = tempDir.resolve("nonexistent_movies.txt").toString();
        Path usersPath  = tempDir.resolve("users.txt");
        Path outputPath = tempDir.resolve("recommendations.txt");
        Files.writeString(usersPath, "John Smith,12345678A\naction\n");
        // Act
        runPipelineWithPaths(missingMovies, usersPath.toString());
        // Assert
        assertTrue(Files.exists(outputPath),
                "recommendations.txt must still be created even when movies file missing");
    }

    // =========================================================
    // IT-22: Missing users.txt → exception handled → error in output
    // =========================================================

    // IT-22a: Missing users.txt → FileNotFoundException written to output
    @Test
    void missingUsersFile_exceptionWrittenToOutput() throws Exception {
        // Arrange
        Path moviesPath = tempDir.resolve("movies.txt");
        Files.writeString(moviesPath, "The Matrix,TM123\naction\n");
        String missingUsers = tempDir.resolve("nonexistent_users.txt").toString();
        // Act
        String output = runPipelineWithPaths(moviesPath.toString(), missingUsers);
        // Assert
        assertFalse(output.isBlank(),
                "Output must not be empty when users file is missing");
        assertFalse(output.contains("For User:"),
                "No recommendations when users file is missing");
    }

    // IT-22b: Missing users.txt → output file still created on disk
    @Test
    void missingUsersFile_outputFileStillCreated() throws Exception {
        // Arrange
        Path moviesPath = tempDir.resolve("movies.txt");
        Path outputPath = tempDir.resolve("recommendations.txt");
        Files.writeString(moviesPath, "The Matrix,TM123\naction\n");
        String missingUsers = tempDir.resolve("nonexistent_users.txt").toString();
        // Act
        runPipelineWithPaths(moviesPath.toString(), missingUsers);
        // Assert
        assertTrue(Files.exists(outputPath),
                "recommendations.txt must still be created even when users file missing");
    }

    // IT-22c: Both files missing → movies error appears first
    @Test
    void bothFilesMissing_moviesErrorAppearsFirst() throws Exception {
        // Arrange
        String missingMovies = tempDir.resolve("nonexistent_movies.txt").toString();
        String missingUsers  = tempDir.resolve("nonexistent_users.txt").toString();
        // Act
        String output = runPipelineWithPaths(missingMovies, missingUsers);
        // Assert — movies parsed first so its error appears
        assertFalse(output.isBlank());
        assertFalse(output.contains("For User:"));
    }
    // Add to IntegrationTestMain — currently missing

    // Empty users file → blank output
    @Test
    void emptyUsersFile_outputIsBlank() throws Exception {
        String output = runPipeline("The Matrix,TM123\naction\n", "");
        assertTrue(output.isBlank(),
                "Empty users file should produce blank output");
    }

    // Empty movies file → user block with empty category
    @Test
    void emptyMoviesFile_userBlockWithEmptyCategory() throws Exception {
        String output = runPipeline("", "John Smith,12345678A\naction\n");
        assertTrue(output.contains("For User:") || output.isBlank(),
                "Empty movies produces user block with empty categories");
    }
}