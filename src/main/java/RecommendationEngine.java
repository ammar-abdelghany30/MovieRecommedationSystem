import java.util.*;

public class RecommendationEngine {

    public static String generateRecommendations(List<User> users, List<Movie> movies) {
        StringBuilder sb = new StringBuilder();

        for (User user : users) {
            sb.append("For User: ").append(user.getUsername())
                    .append(", ").append(user.getId()).append("\n");

            for (String category : user.getLikedCategories()) {
                sb.append("{").append(category).append("}:");
                List<String> matches = new ArrayList<>();
                for (Movie movie : movies) {
                    if (movie.getCategories().contains(category)) {
                        matches.add(movie.getId() + "-" + movie.getTitle());
                    }
                }
                sb.append(String.join(", ", matches)).append("\n");
            }
        }

        return sb.toString();
    }
}