import java.io.*;
import java.util.*;

public class FileReader {

    public static List<Movie> readMovies(String filePath) throws Exception {
        List<Movie> movies = new ArrayList<>();
        BufferedReader br = new BufferedReader(new java.io.FileReader(filePath));
        String line;

        while ((line = br.readLine()) != null) {
            // Line 1: title,id
            String[] titleId = line.split(",");
            String title = titleId[0].trim();
            String id = titleId[1].trim();

            // Validate title
            if (!Validator.isValidMovieTitle(title)) {
                br.close();
                throw new Exception("Movie Title ERROR: " + title + " is wrong");
            }

            // Validate ID letters
            if (!Validator.isValidMovieIdLetters(title, id)) {
                br.close();
                throw new Exception("Movie Id letters ERROR: " + id + " are wrong");
            }

            // Validate ID numbers
            if (!Validator.isValidMovieIdNumbers(id)) {
                br.close();
                throw new Exception("Movie Id numbers ERROR: " + id + " aren't unique");
            }

            // Line 2: categories
            String catLine = br.readLine();
            List<String> categories = new ArrayList<>();
            for (String cat : catLine.split(",")) {
                categories.add(cat.trim().toLowerCase());
            }

            movies.add(new Movie(title, id, categories));
        }

        br.close();
        return movies;
    }

    public static List<User> readUsers(String filePath) throws Exception {
        List<User> users = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();
        BufferedReader br = new BufferedReader(new java.io.FileReader(filePath));
        String line;

        while ((line = br.readLine()) != null) {
            String[] nameId = line.split(",");
            String username = nameId[0].trim();
            String userId = nameId[1].trim();

            if (!Validator.isValidUsername(username)) {
                br.close();
                throw new Exception("Username ERROR: " + username + " is wrong");
            }

            if (!Validator.isValidUserId(userId) || seenIds.contains(userId)) {
                br.close();
                throw new Exception("User Id ERROR: " + userId + " is wrong");
            }

            seenIds.add(userId);

            String catLine = br.readLine();
            List<String> liked = new ArrayList<>();
            for (String cat : catLine.split(",")) {
                liked.add(cat.trim().toLowerCase());
            }

            users.add(new User(username, userId, liked));
        }

        br.close();
        return users;
    }
}