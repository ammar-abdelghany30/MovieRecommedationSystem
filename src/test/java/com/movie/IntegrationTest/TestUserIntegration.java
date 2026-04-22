package com.movie.IntegrationTest;

import com.movie.FileReader;
import com.movie.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUserIntegration {

    @TempDir
    Path tempDir;

    private String createTempFile(String fileName, String content) throws IOException {
        Path filePath = tempDir.resolve(fileName);
        Files.writeString(filePath, content);
        return filePath.toString();
    }

    // IT-06: Valid users.txt -> Validator passes -> User objects created correctly
    @Test
    void testIT06_ValidUser() throws Exception {
        String content = "Alice,123456789\nAction, Drama";
        String filePath = createTempFile("valid_user.txt", content);

        List<User> users = FileReader.readUsers(filePath);

        assertEquals(1, users.size());
        User user = users.get(0);
        assertEquals("Alice", user.getUsername());
        assertEquals("123456789", user.getId());
        assertEquals(List.of("action", "drama"), user.getLikedCategories());
    }

    // IT-07: Invalid username -> Validator rejects -> correct error thrown
    @Test
    void testIT07_InvalidUsername() throws IOException {
        String content = "Alice123,123456789\nAction, Drama"; 
        String filePath = createTempFile("invalid_username.txt", content);

        Exception exception = assertThrows(Exception.class, () -> {
            FileReader.readUsers(filePath);
        });
        assertTrue(exception.getMessage().contains("Username ERROR: Alice123 is wrong"));
    }

    // IT-08: Invalid userID -> Validator rejects -> correct error thrown
    @Test
    void testIT08_InvalidUserId() throws IOException {
        String content = "Alice,12345678\nAction, Drama"; 
        String filePath = createTempFile("invalid_userid.txt", content);

        Exception exception = assertThrows(Exception.class, () -> {
            FileReader.readUsers(filePath);
        });
        assertTrue(exception.getMessage().contains("User Id ERROR: 12345678 is wrong"));
    }

    // IT-09: Duplicate userIDs -> HashSet catches it -> correct error thrown
    @Test
    void testIT09_DuplicateUserIds() throws IOException {
        String content = "Alice,123456789\nAction, Drama\nBob,123456789\nComedy";
        String filePath = createTempFile("duplicate_userid.txt", content);

        Exception exception = assertThrows(Exception.class, () -> {
            FileReader.readUsers(filePath);
        });
        assertTrue(exception.getMessage().contains("User Id ERROR: 123456789 is wrong"));
    }

    // IT-10: First user valid, second invalid -> first error only rule
    @Test
    void testIT10_FirstValidSecondInvalid() throws IOException {
        String content = "Alice,123456789\nAction\nBob123,12345678A\nComedy"; 
        String filePath = createTempFile("first_valid_second_invalid.txt", content);

        Exception exception = assertThrows(Exception.class, () -> {
            FileReader.readUsers(filePath);
        });
        assertTrue(exception.getMessage().contains("Username ERROR: Bob123 is wrong"));
    }
}
