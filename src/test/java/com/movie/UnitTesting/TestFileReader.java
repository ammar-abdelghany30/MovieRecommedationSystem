package com.movie.UnitTesting;

import com.movie.FileReader;
import com.movie.Movie;
import com.movie.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TestFileReader {

    @TempDir
    Path tempDir;

    // =========================================================
    // PART 1: MOVIE PARSING TESTS
    // =========================================================

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
    void testMovieCategoriesTrimmedAndLowercased() throws Exception {
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
    void testMovieMissingCategoryLine_throwsNPE() throws Exception {
        String content = "The Matrix, TM123\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(NullPointerException.class, () -> FileReader.readMovies(file.toString()));
        assertTrue(ex.getMessage().contains("Missing Category!"));
    }

    // Error: first line missing comma -> ArrayIndexOutOfBoundsException
    @Test
    void testMovieFirstLineMissingComma_throwsException() throws Exception {
        String content = "The Matrix TM123\naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> FileReader.readMovies(file.toString()));
    }

    // Edge: empty file returns empty list
    @Test
    void testEmptyMovieFile_returnsEmptyList() throws Exception {
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, "");
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertTrue(movies.isEmpty());
    }

    // Error: file not found -> FileNotFoundException
    @Test
    void testMovieFileDoesNotExist_throwsException() {
        String nonExistent = tempDir.resolve("missing.txt").toString();
        Exception ex = assertThrows(Exception.class, () -> FileReader.readMovies(nonExistent));
        assertTrue(ex instanceof java.io.FileNotFoundException);
    }

    // Edge: extra spaces around title and ID are trimmed
    @Test
    void testMovieExtraSpacesTrimmed() throws Exception {
        String content = "  The Matrix ,  TM123  \naction\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        assertEquals("The Matrix", movies.get(0).getTitle());
        assertEquals("TM123", movies.get(0).getId());
    }

    // Edge: trailing comma in category line creates empty string category (unintended behavior)
    @Test
    void testMovieTrailingCommaInCategoryLine() throws Exception {
        String content = "The Matrix, TM123\naction,sci-fi,\n";
        Path file = tempDir.resolve("movies.txt");
        Files.writeString(file, content);
        List<Movie> movies = FileReader.readMovies(file.toString());
        List<String> cats = movies.get(0).getCategories();
        assertEquals(2, cats.size());
    }


    // =========================================================
    // PART 2: USER PARSING TESTS
    // =========================================================

    // Happy path: single valid user returns correct username
    @Test
    void testSingleValidUser_correctUsername() throws Exception {
        String content = "John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(1, users.size());
        assertEquals("John Smith", users.get(0).getUsername());
    }

    // Happy path: single valid user returns correct ID
    @Test
    void testSingleValidUser_correctId() throws Exception {
        String content = "John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("12345678A", users.get(0).getId());
    }

    // Happy path: single valid user returns correct liked categories
    @Test
    void testSingleValidUser_correctCategories() throws Exception {
        String content = "John Smith,12345678A\naction,drama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertTrue(users.get(0).getLikedCategories().contains("action"));
        assertTrue(users.get(0).getLikedCategories().contains("drama"));
    }

    // Happy path: user ID with no trailing letter (all digits) is valid
    @Test
    void testValidUser_allDigitId() throws Exception {
        String content = "Alice Brown,123456789\ncomedy\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(1, users.size());
        assertEquals("123456789", users.get(0).getId());
    }

    // Happy path: single-word username is valid
    @Test
    void testSingleWordUsername_isValid() throws Exception {
        String content = "Alice,123456789\nhorror\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(1, users.size());
        assertEquals("Alice", users.get(0).getUsername());
    }

    // Happy path: multiple valid users are all returned
    @Test
    void testMultipleValidUsers_correctCount() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,987654321\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(2, users.size());
    }

    // Happy path: multiple valid users have correct usernames
    @Test
    void testMultipleValidUsers_correctUsernames() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,987654321\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("John Smith", users.get(0).getUsername());
        assertEquals("Alice Brown", users.get(1).getUsername());
    }

    // Happy path: multiple valid users have correct IDs
    @Test
    void testMultipleValidUsers_correctIds() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,987654321\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("12345678A", users.get(0).getId());
        assertEquals("987654321", users.get(1).getId());
    }

    // Happy path: three valid users are all parsed correctly
    @Test
    void testThreeValidUsers_correctCount() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,987654321\ndrama\nBob Lee,112233445\nsci-fi\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(3, users.size());
    }

    // Trimming: extra spaces around username and ID are trimmed
    @Test
    void testExtraSpacesAroundNameAndId_areTrimmed() throws Exception {
        String content = "  John Smith  ,  12345678A  \naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("John Smith", users.get(0).getUsername());
        assertEquals("12345678A", users.get(0).getId());
    }

    // Trimming: categories are trimmed and lowercased
    @Test
    void testUserCategories_trimmedAndLowercased() throws Exception {
        String content = "John Smith,12345678A\n  ACTION ,  DRAMA  \n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(List.of("action", "drama"), users.get(0).getLikedCategories());
    }

    // Trimming: single category trimmed and lowercased
    @Test
    void testSingleCategory_trimmedAndLowercased() throws Exception {
        String content = "John Smith,12345678A\n  HORROR  \n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("horror", users.get(0).getLikedCategories().get(0));
    }

    // Error: username starts with a digit -> Username ERROR
    @Test
    void testUsernameStartsWithDigit_throwsError() {
        String content = "1John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        assertThrows(Exception.class, () -> {
            Files.writeString(file, content);
            Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
            assertTrue(ex.getMessage().contains("Username ERROR"));
        });
    }

    // Error: username starts with a digit -> Username ERROR (direct check)
    @Test
    void testUsernameStartsWithDigit_errorMessage() throws Exception {
        String content = "1John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }

    // Error: username contains a number in middle -> Username ERROR
    @Test
    void testUsernameContainsDigit_throwsError() throws Exception {
        String content = "John2 Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }

    // Error: username contains special character -> Username ERROR
    @Test
    void testUsernameContainsSpecialChar_throwsError() throws Exception {
        String content = "John@Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }

    // Error: empty username -> Username ERROR
    @Test
    void testEmptyUsername_throwsError() throws Exception {
        String content = ",12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }

    // Error: username is only spaces -> Username ERROR
    @Test
    void testUsernameOnlySpaces_throwsError() throws Exception {
        String content = "   ,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }

    // Username leading space trims to valid
    @Test
    void testUsernameLeadingSpace_afterTrimIsValid() throws Exception {
        String content = " John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("John Smith", users.get(0).getUsername());
    }

    // Error: user ID shorter than 9 chars -> User Id ERROR
    @Test
    void testUserIdTooShort_throwsError() throws Exception {
        String content = "John Smith,1234567A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Error: user ID longer than 9 chars -> User Id ERROR
    @Test
    void testUserIdTooLong_throwsError() throws Exception {
        String content = "John Smith,123456789AB\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Error: user ID starts with a letter instead of digit -> User Id ERROR
    @Test
    void testUserIdStartsWithLetter_throwsError() throws Exception {
        String content = "John Smith,A12345678\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Error: user ID has more than one letter -> User Id ERROR
    @Test
    void testUserIdTwoLettersAtEnd_throwsError() throws Exception {
        String content = "John Smith,1234567AB\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Error: user ID has a letter in the middle -> User Id ERROR
    @Test
    void testUserIdLetterInMiddle_throwsError() throws Exception {
        String content = "John Smith,1234A5678\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Error: user ID contains special character -> User Id ERROR
    @Test
    void testUserIdContainsSpecialChar_throwsError() throws Exception {
        String content = "John Smith,12345678@\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Error: empty user ID -> User Id ERROR
    @Test
    void testEmptyUserId_throwsError() throws Exception {
        String content = "John Smith, \naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Duplicate: two users with the exact same ID -> User Id ERROR
    @Test
    void testDuplicateUserId_throwsError() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,12345678A\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Duplicate: only the first occurrence is accepted; second raises error
    @Test
    void testDuplicateUserId_firstUserSucceeds_secondFails() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,12345678A\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("12345678A"));
    }

    // Duplicate: three users where the third shares ID with the first -> User Id ERROR
    @Test
    void testDuplicateUserId_thirdUserDuplicate_throwsError() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,987654321\ndrama\nBob Lee,12345678A\nhorror\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Duplicate: two users with same ID where ID is all-numeric
    @Test
    void testDuplicateUserId_allNumericId_throwsError() throws Exception {
        String content = "John Smith,123456789\naction\nAlice Brown,123456789\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Non-duplicate: two users with different IDs are both accepted
    @Test
    void testTwoUsersWithDifferentIds_bothAccepted() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,987654321\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(2, users.size());
    }

    // Non-duplicate: IDs that differ only in the last letter are unique
    @Test
    void testTwoUsersIdsWithDifferentTrailingLetter_bothAccepted() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,12345678B\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(2, users.size());
    }

    // Error: first line has no comma -> ArrayIndexOutOfBoundsException
    @Test
    void testUserFirstLineMissingComma_throwsException() throws Exception {
        String content = "John Smith 12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> FileReader.readUsers(file.toString()));
    }

    // Error: missing category line (second line) -> NullPointerException
    @Test
    void testUserMissingCategoryLine_throwsNPE() throws Exception {
        String content = "John Smith,12345678A\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertThrows(NullPointerException.class, () -> FileReader.readUsers(file.toString()));
    }

    // Edge: empty file returns empty list
    @Test
    void testEmptyUserFile_returnsEmptyList() throws Exception {
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, "");
        List<User> users = FileReader.readUsers(file.toString());
        assertTrue(users.isEmpty());
    }

    // Edge: file does not exist -> FileNotFoundException
    @Test
    void testUserFileDoesNotExist_throwsFileNotFoundException() {
        String nonExistent = tempDir.resolve("missing_users.txt").toString();
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(nonExistent));
        assertTrue(ex instanceof java.io.FileNotFoundException);
    }

    // Edge: trailing comma in category line parsed correctly
    @Test
    void testUserTrailingCommaInCategoryLine_parsedWithEmptyEntry() throws Exception {
        String content = "John Smith,12345678A\naction,drama,\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        List<String> liked = users.get(0).getLikedCategories();
        assertEquals(2, liked.size());
    }

    // Edge: single category with no comma is stored correctly
    @Test
    void testUserSingleCategoryNoComma_stored() throws Exception {
        String content = "John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(1, users.get(0).getLikedCategories().size());
        assertEquals("action", users.get(0).getLikedCategories().get(0));
    }

    // Edge: many categories on the liked line are all stored
    @Test
    void testMultipleUserCategories_allStored() throws Exception {
        String content = "John Smith,12345678A\naction,drama,horror,comedy,thriller\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(5, users.get(0).getLikedCategories().size());
    }

    // Boundary: ID of exactly 9 characters (all digits) is accepted
    @Test
    void testUserIdExactly9Digits_isValid() throws Exception {
        String content = "John Smith,123456789\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("123456789", users.get(0).getId());
    }

    // Boundary: ID of exactly 9 characters (8 digits + 1 letter at end) is accepted
    @Test
    void testUserIdExactly9Chars_8DigitsAndOneLetter_isValid() throws Exception {
        String content = "John Smith,12345678Z\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("12345678Z", users.get(0).getId());
    }

    // Boundary: ID of 8 characters is rejected
    @Test
    void testUserIdEightChars_isRejected() throws Exception {
        String content = "John Smith,12345678\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Boundary: ID of 10 characters is rejected
    @Test
    void testUserIdTenChars_isRejected() throws Exception {
        String content = "John   Smith,1234567890\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // All-lowercase username is VALID
    @Test
    void testUsername_allLowercase_isValid() throws Exception {
        String content = "john smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(1, users.size());
        assertEquals("john smith", users.get(0).getUsername());
    }

    // All-uppercase username is VALID
    @Test
    void testUsername_allUppercase_isValid() throws Exception {
        String content = "JOHN SMITH,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("JOHN SMITH", users.get(0).getUsername());
    }

    // Mixed-case username is VALID
    @Test
    void testUsername_mixedCase_isValid() throws Exception {
        String content = "jOhN sMiTh,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("jOhN sMiTh", users.get(0).getUsername());
    }

    // Three-word username is VALID
    @Test
    void testUsername_threeWords_isValid() throws Exception {
        String content = "John Michael Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("John Michael Smith", users.get(0).getUsername());
    }

    // Single-letter username is VALID (minimum length)
    @Test
    void testUsername_singleLetter_isValid() throws Exception {
        String content = "A,123456789\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("A", users.get(0).getUsername());
    }

    // Username error message must contain the bad username value
    @Test
    void testUsername_errorMessageContainsBadValue() throws Exception {
        String content = "John2Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("John2Smith"));
    }

    // Valid: 9 all-digit ID
    @Test
    void testUserId_allDigits9Chars_isValid() throws Exception {
        String content = "John Smith,123456789\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertEquals("123456789", FileReader.readUsers(file.toString()).get(0).getId());
    }

    // Valid: 8 digits + 1 uppercase letter at the last position
    @Test
    void testUserId_8digitsUppercaseLetterAtEnd_isValid() throws Exception {
        String content = "John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertEquals("12345678A", FileReader.readUsers(file.toString()).get(0).getId());
    }

    // Valid: 8 digits + 1 lowercase letter at the last position
    @Test
    void testUserId_8digitsLowercaseLetterAtEnd_isValid() throws Exception {
        String content = "John Smith,12345678z\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertEquals("12345678z", FileReader.readUsers(file.toString()).get(0).getId());
    }

    // User ID error message must contain the bad ID value
    @Test
    void testUserId_errorMessageContainsBadValue() throws Exception {
        String content = "John Smith,BADINPUTX\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("BADINPUTX"));
    }

    // Duplicate error message contains the repeated ID value
    @Test
    void testDuplicate_errorMessageContainsDuplicateId() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,12345678A\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("12345678A"));
    }

    // Same user entered twice consecutively -> error on the second occurrence
    @Test
    void testDuplicate_consecutiveSameUser_throwsOnSecond() throws Exception {
        String content = "John Smith,12345678A\naction\nJohn Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Duplicate detection is case-SENSITIVE
    @Test
    void testDuplicate_caseSensitive_differentCasesMeanDifferentIds() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,12345678a\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(2, users.size());
    }

    // When duplicate is detected the method throws -> caller never gets a partial list
    @Test
    void testDuplicate_noPartialListReturned() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,12345678A\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
    }

    // Five users with completely different IDs -> all five returned
    @Test
    void testDuplicate_fiveUniqueIds_allReturned() throws Exception {
        String content =
                "Alice,123456781\naction\n" +
                        "Bob,123456782\ndrama\n"   +
                        "Carol,123456783\nhorror\n" +
                        "Dave,123456784\nsci-fi\n"  +
                        "Eve,123456785\nthriller\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(5, users.size());
    }

    // First user valid, second user has invalid format -> User Id ERROR on second
    @Test
    void testCombined_validThenInvalidFormat_throwsUserIdError() throws Exception {
        String content = "John Smith,12345678A\naction\nAlice Brown,TOOSHORT\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Invalid username on first user -> method stops BEFORE ever checking the ID
    @Test
    void testCombined_badUsername_stopsBeforeIdCheck() throws Exception {
        String content = "John2Smith,12345678A\naction\nAlice Brown,987654321\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
        assertFalse(ex.getMessage().contains("User Id ERROR"));
    }

    // Invalid format ID that is also a duplicate -> still "User Id ERROR" (same branch)
    @Test
    void testCombined_invalidFormatAndDuplicate_sameErrorThrown() throws Exception {
        String content = "John Smith,BADINPUTX\naction\nAlice Brown,BADINPUTX\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

}