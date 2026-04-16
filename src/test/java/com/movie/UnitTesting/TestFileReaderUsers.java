package com.movie.UnitTesting;

import com.movie.FileReader;
import com.movie.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Member 4 — Unit tests for FileReader.readUsers()
 * Covers: users.txt parsing + duplicate ID detection
 */
public class TestFileReaderUsers {

    @TempDir
    Path tempDir;

    // =========================================================
    // SCENARIO 1: Happy Path — Valid Single User
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

    // =========================================================
    // SCENARIO 2: Happy Path — Multiple Valid Users
    // =========================================================

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

    // =========================================================
    // SCENARIO 3: Whitespace / Trimming Behavior
    // =========================================================

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
    void testCategories_trimmedAndLowercased() throws Exception {
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

    // =========================================================
    // SCENARIO 4: Username Validation Errors
    // =========================================================

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
        // After trim, username becomes empty, which fails isValidUsername
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }

    // Error: username starts with space (before trimming, readUsers trims it — but Validator
    //        checks charAt(0) before trim; the code does trim() before calling Validator)
    //        -> actually username.trim() is called so leading space is removed first.
    //        This test documents the trimming behavior: a name " John" becomes "John" which is valid.
    @Test
    void testUsernameLeadingSpace_afterTrimIsValid() throws Exception {
        String content = " John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        // After trim the name is "John Smith" -> valid
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals("John Smith", users.get(0).getUsername());
    }

    // =========================================================
    // SCENARIO 5: User ID Format Validation Errors
    // =========================================================

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

    // Error: user ID has more than one letter (two letters at end) -> User Id ERROR
    @Test
    void testUserIdTwoLettersAtEnd_throwsError() throws Exception {
        String content = "John Smith,1234567AB\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // Error: user ID has a letter in the middle (not at the last position) -> User Id ERROR
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
        String content = "John Smith,\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }

    // =========================================================
    // SCENARIO 6: Duplicate ID Detection
    // =========================================================

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
        // The duplicate ID value appears in the error message
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

    // =========================================================
    // SCENARIO 7: Structural / Format Errors
    // =========================================================

    // Error: first line has no comma -> ArrayIndexOutOfBoundsException
    @Test
    void testFirstLineMissingComma_throwsException() throws Exception {
        String content = "John Smith 12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> FileReader.readUsers(file.toString()));
    }

    // Error: missing category line (second line) -> NullPointerException
    @Test
    void testMissingCategoryLine_throwsNPE() throws Exception {
        String content = "John Smith,12345678A\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertThrows(NullPointerException.class, () -> FileReader.readUsers(file.toString()));
    }

    // =========================================================
    // SCENARIO 8: Edge Cases — Empty File / File Not Found
    // =========================================================

    // Edge: empty file returns empty list
    @Test
    void testEmptyFile_returnsEmptyList() throws Exception {
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, "");
        List<User> users = FileReader.readUsers(file.toString());
        assertTrue(users.isEmpty());
    }

    // Edge: file does not exist -> FileNotFoundException
    @Test
    void testFileDoesNotExist_throwsFileNotFoundException() {
        String nonExistent = tempDir.resolve("missing_users.txt").toString();
        Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(nonExistent));
        assertTrue(ex instanceof java.io.FileNotFoundException);
    }

    // =========================================================
    // SCENARIO 9: Edge Cases — Category Line Quirks
    // =========================================================

    // Edge: trailing comma in category line — documents split behavior (produces empty entry)
    @Test
    void testTrailingCommaInCategoryLine_parsedWithEmptyEntry() throws Exception {
        String content = "John Smith,12345678A\naction,drama,\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        List<String> liked = users.get(0).getLikedCategories();
        // "action,drama,".split(",") gives ["action","drama"] — no empty trailing element
        assertEquals(2, liked.size());
    }

    // Edge: single category with no comma is stored correctly
    @Test
    void testSingleCategoryNoComma_stored() throws Exception {
        String content = "John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(1, users.get(0).getLikedCategories().size());
        assertEquals("action", users.get(0).getLikedCategories().get(0));
    }

    // Edge: many categories on the liked line are all stored
    @Test
    void testMultipleCategories_allStored() throws Exception {
        String content = "John Smith,12345678A\naction,drama,horror,comedy,thriller\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(5, users.get(0).getLikedCategories().size());
    }

    // =========================================================
    // SCENARIO 10: Boundary — ID exactly 9 chars
    // =========================================================

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

  // -----------------------------------------------------------//

     // =================================================================
    // SCENARIO 1 — Username Validation (isValidUsername rules)
    // Rule: [a-zA-Z ]+ only, NOT empty, must NOT begin with a space.
    // KEY DIFFERENCE from movies: NO capital-letter requirement.
    // =================================================================
    // All-lowercase username is VALID (no capital letter rule like movie titles)
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
    // Username that starts with a digit -> Username ERROR
    @Test
    void testUsername_startsWithDigit_throwsUsernameError() throws Exception {
        String content = "1John,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }
    // Username that contains a digit in the middle -> Username ERROR
    @Test
    void testUsername_digitInMiddle_throwsUsernameError() throws Exception {
        String content = "Jo2hn Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }
    // Username that contains a special character -> Username ERROR
    @Test
    void testUsername_specialChar_throwsUsernameError() throws Exception {
        String content = "John@Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }
    // Empty username (nothing before the comma) -> Username ERROR
    @Test
    void testUsername_empty_throwsUsernameError() throws Exception {
        String content = ",12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }
    // Username that is only spaces -> after trim becomes empty -> Username ERROR
    @Test
    void testUsername_onlySpaces_throwsUsernameError() throws Exception {
        String content = "    ,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
    }
    // Username error message must contain the bad username value
    @Test
    void testUsername_errorMessageContainsBadValue() throws Exception {
        String content = "John2Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        // format: "Username ERROR: <value> is wrong"
        assertTrue(ex.getMessage().contains("John2Smith"));
    }
    // =================================================================
    // SCENARIO 2 — User ID Validation (isValidUserId rules)
    // Rule: exactly 9 chars, alphanumeric, starts with digit,
    //       at most ONE letter and it MUST be at position 8 (last).
    // KEY DIFFERENCE from movies: completely different ID structure.
    // =================================================================
    // Valid: 9 all-digit ID
    @Test
    void testUserId_allDigits9Chars_isValid() throws Exception {
        String content = "John Smith,123456789\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertEquals("123456789",
                FileReader.readUsers(file.toString()).get(0).getId());
    }
    // Valid: 8 digits + 1 uppercase letter at the last position
    @Test
    void testUserId_8digitsUppercaseLetterAtEnd_isValid() throws Exception {
        String content = "John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertEquals("12345678A",
                FileReader.readUsers(file.toString()).get(0).getId());
    }
    // Valid: 8 digits + 1 lowercase letter at the last position
    @Test
    void testUserId_8digitsLowercaseLetterAtEnd_isValid() throws Exception {
        String content = "John Smith,12345678z\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertEquals("12345678z",
                FileReader.readUsers(file.toString()).get(0).getId());
    }
    // Invalid: 8 chars (one too short) -> User Id ERROR
    @Test
    void testUserId_8chars_throwsUserIdError() throws Exception {
        String content = "John Smith,12345678\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid: 10 chars (one too long) -> User Id ERROR
    @Test
    void testUserId_10chars_throwsUserIdError() throws Exception {
        String content = "John Smith,1234567890\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid: starts with a letter instead of a digit -> User Id ERROR
    @Test
    void testUserId_startsWithLetter_throwsUserIdError() throws Exception {
        String content = "John Smith,A12345678\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid: letter is at position 4 (middle), not at the last position -> User Id ERROR
    @Test
    void testUserId_letterInMiddle_throwsUserIdError() throws Exception {
        String content = "John Smith,1234A5678\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid: two letters at the end -> User Id ERROR
    @Test
    void testUserId_twoLettersAtEnd_throwsUserIdError() throws Exception {
        String content = "John Smith,1234567AB\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid: special character inside ID -> User Id ERROR
    @Test
    void testUserId_specialChar_throwsUserIdError() throws Exception {
        String content = "John Smith,12345678!\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid: empty ID (nothing after comma) -> User Id ERROR
    @Test
    void testUserId_empty_throwsUserIdError() throws Exception {
        String content = "John Smith,\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // User ID error message must contain the bad ID value
    @Test
    void testUserId_errorMessageContainsBadValue() throws Exception {
        String content = "John Smith,BADINPUTX\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        // format: "User Id ERROR: <id> is wrong"
        assertTrue(ex.getMessage().contains("BADINPUTX"));
    }
    // =================================================================
    // SCENARIO 3 — Duplicate ID Detection (seenIds HashSet)
    // This whole scenario has NO equivalent in Member 3's readMovies() tests.
    // readMovies() has no duplicate detection at all.
    // =================================================================
    // Two users with the same ID -> User Id ERROR on the second
    @Test
    void testDuplicate_twoSameIds_throwsUserIdError() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "Alice Brown,12345678A\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Duplicate error message contains the repeated ID value
    @Test
    void testDuplicate_errorMessageContainsDuplicateId() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "Alice Brown,12345678A\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("12345678A"));
    }
    // Third user duplicates the FIRST (not the immediately previous one)
    @Test
    void testDuplicate_thirdDuplicatesFirst_throwsUserIdError() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "Alice Brown,987654321\ndrama\n"
                       + "Bob Lee,12345678A\nhorror\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Same user entered twice consecutively -> error on the second occurrence
    @Test
    void testDuplicate_consecutiveSameUser_throwsOnSecond() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "John Smith,12345678A\naction\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // All-digit duplicate ID is also caught by seenIds
    @Test
    void testDuplicate_allDigitId_throwsUserIdError() throws Exception {
        String content = "John Smith,123456789\naction\n"
                       + "Alice Brown,123456789\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Duplicate detection is case-SENSITIVE: "12345678A" and "12345678a" are NOT duplicates
    @Test
    void testDuplicate_caseSensitive_differentCasesMeanDifferentIds() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "Alice Brown,12345678a\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        // HashSet.contains() is case-sensitive -> two distinct IDs -> both accepted
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(2, users.size());
    }
    // IDs differing only in the trailing letter are NOT duplicates -> both accepted
    @Test
    void testDuplicate_differentTrailingLetter_bothAccepted() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "Alice Brown,12345678B\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        List<User> users = FileReader.readUsers(file.toString());
        assertEquals(2, users.size());
    }
    // When duplicate is detected the method throws -> caller never gets a partial list
    @Test
    void testDuplicate_noPartialListReturned() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "Alice Brown,12345678A\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
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
    // =================================================================
    // SCENARIO 4 — Combined condition in one if()
    // Code: if (!Validator.isValidUserId(userId) || seenIds.contains(userId))
    // Both invalid-format and duplicate produce the same "User Id ERROR".
    // =================================================================
    // First user valid, second user has invalid format -> User Id ERROR on second
    @Test
    void testCombined_validThenInvalidFormat_throwsUserIdError() throws Exception {
        String content = "John Smith,12345678A\naction\n"
                       + "Alice Brown,TOOSHORT\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid username on first user -> method stops BEFORE ever checking the ID
    @Test
    void testCombined_badUsername_stopsBeforeIdCheck() throws Exception {
        String content = "John2Smith,12345678A\naction\n"
                       + "Alice Brown,987654321\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("Username ERROR"));
        assertFalse(ex.getMessage().contains("User Id ERROR"));
    }
    // Invalid format ID that is also a duplicate -> still "User Id ERROR" (same branch)
    @Test
    void testCombined_invalidFormatAndDuplicate_sameErrorThrown() throws Exception {
        String content = "John Smith,BADINPUTX\naction\n"
                       + "Alice Brown,BADINPUTX\ndrama\n";
        Path file = tempDir.resolve("users.txt");
        Files.writeString(file, content);
        Exception ex = assertThrows(Exception.class,
                () -> FileReader.readUsers(file.toString()));
        assertTrue(ex.getMessage().contains("User I ERROR"));
    }

}
