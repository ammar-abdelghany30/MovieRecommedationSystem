import java.util.List;

public class User {
    private String username;
    private String id;
    private List<String> likedCategories;

    public User(String username, String id, List<String> likedCategories) {
        this.username = username;
        this.id = id;
        this.likedCategories = likedCategories;
    }

    public String getUsername() { return username; }
    public String getId() { return id; }
    public List<String> getLikedCategories() { return likedCategories; }
}