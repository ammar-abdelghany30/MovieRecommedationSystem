import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Validator {

    private static final Set<String> ALLOWED_CATEGORIES = new HashSet<>(
            List.of("horror", "action", "drama", "comedy", "thriller", "romance", "sci-fi")
    );

    // Movie title: every word must start with a capital letter
    public static boolean isValidMovieTitle(String title) {
        if (title == null || title.isEmpty()) return false;
        String[] words = title.split(" ");
        for (String word : words) {
            if (!Character.isUpperCase(word.charAt(0))) return false;
        }
        return true;
    }

    // Movie ID: capital letters from title + 3 unique digits
    public static boolean isValidMovieIdLetters(String title, String id) {
        // Extract capital letters from title (first letter of each word)
        StringBuilder expected = new StringBuilder();
        for (String word : title.split(" ")) {
            expected.append(word.charAt(0)); // already uppercase if title is valid
        }
        // ID must start with those letters
        String idLetters = id.replaceAll("[0-9]", "");
        return idLetters.equals(expected.toString());
    }

    public static boolean isValidMovieIdNumbers(String id) {
        // Last 3 characters must be digits and all unique
        String digits = id.replaceAll("[^0-9]", "");
        if (digits.length() != 3) return false;
        // All 3 digits must be unique
        return digits.charAt(0) != digits.charAt(1) &&
                digits.charAt(1) != digits.charAt(2) &&
                digits.charAt(0) != digits.charAt(2);
    }

    public static boolean isValidCategory(String category) {
        return ALLOWED_CATEGORIES.contains(category.trim().toLowerCase());
    }

    // Username: alphabetic + spaces only, must not begin with a space
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) return false;
        if (username.charAt(0) == ' ') return false;
        return username.matches("[a-zA-Z ]+");
    }

    // User ID: alphanumeric, exactly 9 chars, starts with numbers, at most one letter at end
    public static boolean isValidUserId(String userId) {
        if (userId == null || userId.length() != 9) return false;
        if (!userId.matches("[a-zA-Z0-9]+")) return false;
        // Must start with a digit
        if (!Character.isDigit(userId.charAt(0))) return false;
        // At most one alphabetic character, and it must be at the end
        long letterCount = userId.chars().filter(Character::isAlphabetic).count();
        if (letterCount > 1) return false;
        if (letterCount == 1 && !Character.isAlphabetic(userId.charAt(8))) return false;
        return true;
    }
}