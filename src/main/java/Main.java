import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String moviesPath = "src/main/resources/movies.txt";
        String usersPath = "src/main/resources/users.txt";
        String outputPath = "src/main/resources/recommendations.txt";

        try {
            List<Movie> movies = FileReader.readMovies(moviesPath);
            List<User> users = FileReader.readUsers(usersPath);

            String output = RecommendationEngine.generateRecommendations(users, movies);

            PrintWriter writer = new PrintWriter(outputPath);
            writer.print(output);
            writer.close();

            System.out.println("Recommendations generated successfully!");

        } catch (Exception e) {
            try {
                PrintWriter writer = new PrintWriter(outputPath);
                writer.println(e.getMessage());
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            System.out.println("Error: " + e.getMessage());
        }
    }
}