package com.movie.BlackBoxTesting.decision_table;

import com.movie.*;
import com.movie.FileReader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================================
 *                    BLACK-BOX DECISION TABLE TESTING
 *              Movie Recommendation System — FileReader
 * ============================================================================
 *
 *  CONDITIONS
 *  ----------
 *   C1 : Movie title valid?         (every word starts with uppercase)
 *   C2 : Movie ID letters valid?    (letters match first letter of each word)
 *   C3 : Movie ID digits valid?     (exactly 3 digits, all unique)
 *   C4 : Username valid?            (alphabetic + spaces, no leading space)
 *   C5 : User ID valid & unique?    (9 chars, starts with digit, ≤1 letter
 *                                     at the end, not already seen)
 *
 *  ACTIONS
 *  -------
 *   A1 : Throw "Movie Title ERROR"
 *   A2 : Throw "Movie Id letters ERROR"
 *   A3 : Throw "Movie Id numbers ERROR"
 *   A4 : Throw "Username ERROR"
 *   A5 : Throw "User Id ERROR"
 *   A6 : Generate recommendations successfully
 *
 *  DECISION TABLE
 *  --------------
 *   Note: "—" = don't care (condition never evaluated due to early
 *   termination — previous validation already threw).
 *
 *   +-------+-----+-----+-----+-----+-----+-----+
 *   | Cond. |  R1 |  R2 |  R3 |  R4 |  R5 |  R6 |
 *   +-------+-----+-----+-----+-----+-----+-----+
 *   |  C1   |  N  |  Y  |  Y  |  Y  |  Y  |  Y  |
 *   |  C2   |  —  |  N  |  Y  |  Y  |  Y  |  Y  |
 *   |  C3   |  —  |  —  |  N  |  Y  |  Y  |  Y  |
 *   |  C4   |  —  |  —  |  —  |  N  |  Y  |  Y  |
 *   |  C5   |  —  |  —  |  —  |  —  |  N  |  Y  |
 *   +-------+-----+-----+-----+-----+-----+-----+
 *   |  A1   |  X  |     |     |     |     |     |
 *   |  A2   |     |  X  |     |     |     |     |
 *   |  A3   |     |     |  X  |     |     |     |
 *   |  A4   |     |     |     |  X  |     |     |
 *   |  A5   |     |     |     |     |  X  |     |
 *   |  A6   |     |     |     |     |     |  X  |
 *   +-------+-----+-----+-----+-----+-----+-----+
 *
 *  TEST CASE → RULE MAPPING
 *  ------------------------
 *   R1 → rule1_invalidMovieTitle()
 *   R2 → rule2_invalidIdLetters()
 *   R3 → rule3_invalidIdNumbers()
 *   R4 → rule4_invalidUsername()
 *   R5 → rule5_invalidUserId()
 *   R6 → rule6_allValid()
 *
 * ============================================================================
 */
public class FileReaderDecisionTable {

    // JUnit 5 creates a fresh temporary folder for each test and deletes it
    // automatically afterwards — no manual cleanup needed.
    @TempDir
    Path tempDir;

    private String moviesPath;
    private String usersPath;

    @BeforeEach
    void setUp() {
        moviesPath = tempDir.resolve("movies.txt").toString();
        usersPath  = tempDir.resolve("users.txt").toString();
    }

    // Helper: write content to a file
    private void writeFile(String path, String content) throws IOException {
        try (PrintWriter pw = new PrintWriter(path)) {
            pw.print(content);
        }
    }

    // =====================================================================
    // RULE 1 — Movie Title invalid  →  "Movie Title ERROR"
    // -----
    //   C1 = N  |  C2 = —  |  C3 = —  |  C4 = —  |  C5 = —
    //   Action: A1
    // =====================================================================
    @Test
    void rule1_invalidMovieTitle() throws IOException {
        // "the matrix" starts with lowercase 't' → fails isValidMovieTitle
        writeFile(moviesPath, "the matrix,TM123\naction\n");

        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readMovies(moviesPath));

        assertTrue(ex.getMessage().contains("Movie Title ERROR"));
    }

    // =====================================================================
    // RULE 2 — Title OK, ID letters wrong  →  "Movie Id letters ERROR"
    // -----
    //   C1 = Y  |  C2 = N  |  C3 = —  |  C4 = —  |  C5 = —
    //   Action: A2
    // =====================================================================
    @Test
    void rule2_invalidIdLetters() throws IOException {
        // Title "The Matrix" → expected letters "TM", but ID has "XY"
        writeFile(moviesPath, "The Matrix,XY123\naction\n");

        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readMovies(moviesPath));

        assertTrue(ex.getMessage().contains("Movie Id letters ERROR"));
    }

    // =====================================================================
    // RULE 3 — Title & letters OK, digits not unique  →  "Movie Id numbers ERROR"
    // -----
    //   C1 = Y  |  C2 = Y  |  C3 = N  |  C4 = —  |  C5 = —
    //   Action: A3
    // =====================================================================
    @Test
    void rule3_invalidIdNumbers() throws IOException {
        // "TM113" — digits 1,1,3 are NOT all unique
        writeFile(moviesPath, "The Matrix,TM113\naction\n");

        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readMovies(moviesPath));

        assertTrue(ex.getMessage().contains("Movie Id numbers ERROR"));
    }

    // =====================================================================
    // RULE 4 — All movies OK, username invalid  →  "Username ERROR"
    // -----
    //   C1 = Y  |  C2 = Y  |  C3 = Y  |  C4 = N  |  C5 = —
    //   Action: A4
    // =====================================================================
    @Test
    void rule4_invalidUsername() throws Exception {
        // Valid movie so we can reach the users file
        writeFile(moviesPath, "The Matrix,TM123\naction\n");
        // Username "John123" contains digits → fails isValidUsername
        writeFile(usersPath, "John123,123456789\naction\n");

        assertDoesNotThrow(() -> FileReader.readMovies(moviesPath));
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(usersPath));

        assertTrue(ex.getMessage().contains("Username ERROR"));
    }

    // =====================================================================
    // RULE 5 — All movies OK, username OK, user ID invalid  →  "User Id ERROR"
    // -----
    //   C1 = Y  |  C2 = Y  |  C3 = Y  |  C4 = Y  |  C5 = N
    //   Action: A5
    // =====================================================================
    @Test
    void rule5_invalidUserId() throws IOException {
        writeFile(moviesPath, "The Matrix,TM123\naction\n");
        // Username valid ("John Smith"), but ID "ABC123456" starts with letters
        writeFile(usersPath, "John Smith,ABC123456\naction\n");

        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(usersPath));

        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // =====================================================================
    // RULE 6 — Everything valid  →  Recommendations generated
    // -----
    //   C1 = Y  |  C2 = Y  |  C3 = Y  |  C4 = Y  |  C5 = Y
    //   Action: A6
    // =====================================================================
    @Test
    @DisplayName("R6: All inputs valid → recommendations generated")
    void rule6_allValid() throws IOException {
        writeFile(moviesPath, "The Matrix,TM123\naction\n");
        writeFile(usersPath,  "John Smith,123456789\naction\n");

        List<Movie> movies = assertDoesNotThrow(() -> FileReader.readMovies(moviesPath));
        List<User>  users  = assertDoesNotThrow(() -> FileReader.readUsers(usersPath));

        assertEquals(1, movies.size());
        assertEquals(1, users.size());

        String output = RecommendationEngine.generateRecommendations(users, movies);

        // Basic checks on the output
        assertTrue(output.contains("John Smith"));
        assertTrue(output.contains("123456789"));
        assertTrue(output.contains("{action}"));
        assertTrue(output.contains("TM123-The Matrix"));
    }
}