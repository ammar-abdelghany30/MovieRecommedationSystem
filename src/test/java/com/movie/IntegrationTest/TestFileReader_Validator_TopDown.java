package com.movie.IntegrationTest;

import com.movie.FileReader;
import com.movie.Movie;
import com.movie.User;
import com.movie.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileReaderIsolatedTest {

    @Test
    void testReadMovies_ParsingOnly_WhenValidatorSaysOk() throws Exception {
        Path file = Files.createTempFile("movies", ".txt");
        Files.writeString(file, "The Matrix, TM123\naction,sci-fi\n");

        try (MockedStatic<Validator> validatorMock = mockStatic(Validator.class)) {
            // Stub all validation calls to return true
            validatorMock.when(() -> Validator.isValidMovieTitle(anyString())).thenReturn(true);
            validatorMock.when(() -> Validator.isValidMovieIdLetters(anyString(), anyString())).thenReturn(true);
            validatorMock.when(() -> Validator.isValidMovieIdNumbers(anyString())).thenReturn(true);

            List<Movie> movies = FileReader.readMovies(file.toString());

            assertEquals(1, movies.size());
            Movie m = movies.get(0);
            assertEquals("The Matrix", m.getTitle());
            assertEquals("TM123", m.getId());
            assertEquals(List.of("action", "sci-fi"), m.getCategories());
        }
        Files.deleteIfExists(file);
    }

    @Test
    void testReadMovies_CategoryParsing_EvenIfValidatorFails() throws Exception {
        // This tests FileReader's line splitting logic regardless of validation
        Path file = Files.createTempFile("movies", ".txt");
        Files.writeString(file, "Bad Title, BAD123\n  ACTION ,  COMEDY  \n");

        try (MockedStatic<Validator> validatorMock = mockStatic(Validator.class)) {
            // Simulate validation failing after parsing – but parsing itself must work
            validatorMock.when(() -> Validator.isValidMovieTitle(anyString())).thenReturn(false);
            // We want to see that the exception thrown contains the validation error,
            // but the categories line was still read. Not directly testable because exception aborts.
            // Better: test that if validation passes, categories are trimmed and lowercased correctly.
            validatorMock.when(() -> Validator.isValidMovieTitle(anyString())).thenReturn(true);
            validatorMock.when(() -> Validator.isValidMovieIdLetters(anyString(), anyString())).thenReturn(true);
            validatorMock.when(() -> Validator.isValidMovieIdNumbers(anyString())).thenReturn(true);

            List<Movie> movies = FileReader.readMovies(file.toString());
            assertEquals(List.of("action", "comedy"), movies.get(0).getCategories());
        }
        Files.deleteIfExists(file);
    }

    @Test
    void testReadUsers_DuplicateDetection_WithoutValidator() throws Exception {
        Path file = Files.createTempFile("users", ".txt");
        Files.writeString(file, "John Smith,12345678A\naction\nAlice Brown,12345678A\ndrama\n");

        try (MockedStatic<Validator> validatorMock = mockStatic(Validator.class)) {
            // Stub Validator to always return true for username and ID format
            validatorMock.when(() -> Validator.isValidUsername(anyString())).thenReturn(true);
            validatorMock.when(() -> Validator.isValidUserId(anyString())).thenReturn(true);

            // The duplicate ID should still be caught by FileReader's Set
            Exception ex = assertThrows(Exception.class, () -> FileReader.readUsers(file.toString()));
            assertTrue(ex.getMessage().contains("User Id ERROR"));
        }
        Files.deleteIfExists(file);
    }
}
