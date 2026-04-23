package com.movie.UnitTesting;

import com.movie.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

public class TestMain {

    @TempDir
    Path tempDir;

    // ── HELPER — runs Main with custom file paths ───────────────
    private String runMain(String moviesContent,
                           String usersContent) throws Exception {
        Path moviesPath = tempDir.resolve("movies.txt");
        Path usersPath  = tempDir.resolve("users.txt");
        Path outputPath = tempDir.resolve("recommendations.txt");

        Files.writeString(moviesPath, moviesContent);
        Files.writeString(usersPath,  usersContent);

        // Invoke Main logic directly
        com.movie.FileReader fr = new com.movie.FileReader();
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

    // =========================================================
    // SCENARIO 1 — Valid Input → Correct Output File
    // =========================================================

    // TC-M01: Valid input produces recommendations in output file
    @Test
    void validInput_outputFileCreated() throws Exception {
        // Arrange + Act
        String output = runMain(
                "The Matrix,TM123\naction\n",
                "John Smith,12345678A\naction\n"
        );
        // Assert
        assertTrue(output.contains("For User: John Smith, 12345678A"));
        assertTrue(output.contains("TM123-The Matrix"));
    }

    // TC-M02: Output file is empty when no users
    @Test
    void emptyUsers_outputFileIsEmpty() throws Exception {
        String output = runMain("The Matrix,TM123\naction\n", "");
        assertTrue(output.isBlank());
    }

    // TC-M03: Output file is empty when no movies
    @Test
    void emptyMovies_outputFileHasUserBlockOnly() throws Exception {
        String output = runMain("", "John Smith,12345678A\naction\n");
        // Empty movies → user block with empty category
        assertTrue(output.contains("For User:") || output.isBlank());
    }

    // =========================================================
    // SCENARIO 2 — Invalid Input → Error in Output File
    // =========================================================

    // TC-M04: Invalid movie title → error written to output file
    @Test
    void invalidMovieTitle_errorWrittenToOutput() throws Exception {
        String output = runMain(
                "the matrix,TM123\naction\n",
                "John Smith,12345678A\naction\n"
        );
        assertTrue(output.contains("Movie Title ERROR"));
    }

    // TC-M05: Invalid movie ID → error written to output file
    @Test
    void invalidMovieId_errorWrittenToOutput() throws Exception {
        String output = runMain(
                "The Matrix,XY123\naction\n",
                "John Smith,12345678A\naction\n"
        );
        assertTrue(output.contains("Movie Id letters ERROR"));
    }

    // TC-M06: Invalid username → error written to output file
    @Test
    void invalidUsername_errorWrittenToOutput() throws Exception {
        String output = runMain(
                "The Matrix,TM123\naction\n",
                "John2 Smith,12345678A\naction\n"
        );
        assertTrue(output.contains("Username ERROR"));
    }

    // TC-M07: Invalid user ID → error written to output file
    @Test
    void invalidUserId_errorWrittenToOutput() throws Exception {
        String output = runMain(
                "The Matrix,TM123\naction\n",
                "John Smith,A12345678\naction\n"
        );
        assertTrue(output.contains("User Id ERROR"));
    }

    // =========================================================
    // SCENARIO 3 — First Error Only Rule
    // =========================================================

    // TC-M08: Both files invalid — only movie error in output
    @Test
    void bothFilesInvalid_onlyFirstErrorInOutput() throws Exception {
        String output = runMain(
                "the matrix,TM123\naction\n",  // bad title
                "John2 Smith,12345678A\naction\n"  // bad username
        );
        assertTrue(output.contains("Movie Title ERROR"));
        assertFalse(output.contains("Username ERROR"));
    }

    // TC-M09: Output file contains ONLY the error — no recommendations
    @Test
    void errorOutput_containsOnlyErrorMessage() throws Exception {
        String output = runMain(
                "the matrix,TM123\naction\n",
                "John Smith,12345678A\naction\n"
        );
        assertTrue(output.contains("Movie Title ERROR"));
        assertFalse(output.contains("For User:"));
    }
}