package com.movie.UnitTesting;

import com.movie.FileReader;
import com.movie.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestFileReader {

    @TempDir
    Path tempDir;

    // Happy path: single valid movie returns correct fields
    @Test
    void testSingleValidMovie() throws Exception {
        String content = "The Matrix, TM123\naction,sci-fi\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertEquals(1, movies.size());
        Movie m = movies.get(0);
        assertEquals("The Matrix", m.getTitle());
        assertEquals("TM123", m.getId());
        assertTrue(m.getCategories().contains("action"));
        assertTrue(m.getCategories().contains("sci-fi"));
    }

    // Happy path: multiple valid movies all returned
    @Test
    void testMultipleValidMovies() throws Exception {
        String content = "The Matrix, TM123\naction,sci-fi\nInception, I987\nthriller,sci-fi\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertEquals(2, movies.size());
        assertEquals("The Matrix", movies.get(0).getTitle());
        assertEquals("Inception", movies.get(1).getTitle());
    }

    // Happy path: categories trimmed and lowercased
    @Test
    void testCategoriesTrimmedAndLowercased() throws Exception {
        String content = "The Matrix, TM123\n  ACTION ,  SCI-FI  \n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertEquals(List.of("action", "sci-fi"), movies.get(0).getCategories());
    }

    // Happy path: single word title works
    @Test
    void testSingleWordTitle() throws Exception {
        String content = "Inception, I987\nthriller\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertEquals(1, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
    }

    // Error: title first word lowercase -> Movie Title ERROR
    @Test
    void testTitleFirstWordLowerCase_throwsError() throws Exception {
        String content = "the Matrix, TM123\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Title ERROR"));
    }

    // Error: title inner word lowercase -> Movie Title ERROR
    @Test
    void testTitleWordInsideLowerCase_throwsError() throws Exception {
        String content = "The matrix, TM123\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Title ERROR"));
    }

    // Error: empty title (missing before comma) -> Movie Title ERROR
    @Test
    void testEmptyTitle_throwsError() throws Exception {
        String content = ", TM123\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Title ERROR"));
    }

    // Error: ID letters mismatch -> Movie Id letters ERROR
    @Test
    void testIdLettersMismatch_throwsError() throws Exception {
        String content = "The Matrix, TX123\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Id letters ERROR"));
    }

    // Error: ID has no letters -> Movie Id letters ERROR
    @Test
    void testIdLettersMissing_throwsError() throws Exception {
        String content = "The Matrix, 12345\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Id letters ERROR"));
    }

    // Error: ID numbers not unique -> Movie Id numbers ERROR
    @Test
    void testIdNumbersNotUnique_throwsError() throws Exception {
        String content = "The Matrix, TM111\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Id numbers ERROR"));
    }

    // Error: ID numbers less than 3 digits -> Movie Id numbers ERROR
    @Test
    void testIdNumbersLessThanThreeDigits_throwsError() throws Exception {
        String content = "The Matrix, TM12\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Id numbers ERROR"));
    }

    // Error: ID numbers more than 3 digits -> Movie Id numbers ERROR
    @Test
    void testIdNumbersMoreThanThreeDigits_throwsError() throws Exception {
        String content = "The Matrix, TM1234\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Movie Id numbers ERROR"));
    }

    // Error: missing category line (second line) -> NullPointerException (bug)
    @Test
    void testMissingCategoryLine_throwsNPE() throws Exception {
        String content = "The Matrix, TM123\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(NullPointerException.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Missing Category!"));
    }

    // Error: first line missing comma -> ArrayIndexOutOfBoundsException
    @Test
    void testFirstLineMissingComma_throwsException() throws Exception {
        String content = "The Matrix TM123\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> FileReader.readMovies(file.toString()));
    }

    // Edge: empty file returns empty list
    @Test
    void testEmptyFile_returnsEmptyList() throws Exception {
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, "");
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertTrue(movies.isEmpty());
    }

    // Error: file not found -> FileNotFoundException
    @Test
    void testFileDoesNotExist_throwsException() {
        String nonExistent = tempDir.resolve("missing.txt").toString();
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(nonExistent));
        assertTrue(ex instanceof java.io.FileNotFoundException);
    }

    // Edge: extra spaces around title and ID are trimmed
    @Test
    void testExtraSpacesTrimmed() throws Exception {
        String content = "  The Matrix ,  TM123  \naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertEquals("The Matrix", movies.get(0).getTitle());
        assertEquals("TM123", movies.get(0).getId());
    }

    // Edge: trailing comma in category line creates empty string category (unintended behavior)
    @Test
    void testTrailingCommaInCategoryLine() throws Exception {
        String content = "The Matrix, TM123\naction,sci-fi,\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        List<String> cats = movies.get(0).getCategories();
        assertEquals(2, cats.size());
    }
    // Happy path: categories trimmed and lowercased
    @Test
    void testCategoriesTrimmedAndLwercased() throws Exception {
        String content = "The Matrix, TM123\n  ACTION ,  SCI-FI  \n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertEquals(List.of("action", "sci-fi"), movies.get(0).getCategories());
    }
}
