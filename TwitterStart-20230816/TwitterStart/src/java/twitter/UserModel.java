package twitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    // Utility method to get a DB connection
    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        return DatabaseConnection.getConnection();
    }

    // Utility method to extract a User from a ResultSet
    private static User extractUserFromResultSet(ResultSet results) throws SQLException {
        int id = results.getInt("id");
        String username = results.getString("username");
        String password = results.getString("password");
        String filename = results.getString("filename");
        return new User(id, username, password, filename);
    }

    // Login method
    public static User login(User inputUser) throws SQLException, ClassNotFoundException {
        final String query = "SELECT id, username, password, filename FROM user WHERE username=?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, inputUser.getUsername());
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    User user = extractUserFromResultSet(results);
                    if (inputUser.getPassword().equals(user.getPassword())) {
                        return user;
                    }
                }
            }
        }
        return null;
    }

    public static User getUser(String username) throws SQLException, ClassNotFoundException {
        final String query = "SELECT id, username, password, filename FROM user WHERE username=?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return extractUserFromResultSet(results);
                }
            }
        }
        return null;
    }

    public static List<User> getUsers() throws SQLException, ClassNotFoundException {
        final String query = "SELECT * FROM user";
        
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                users.add(extractUserFromResultSet(results));
            }
        }
        return users;
    }

    // Insert a new user
    public static void addUser(User user) throws SQLException, ClassNotFoundException {
        final String query = "INSERT INTO user (username, password) VALUES (?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.execute();
        }
    }

    // Update an existing user
    public static void updateUser(User user) throws SQLException, ClassNotFoundException {
        final String query = "UPDATE user SET username=?, password=? WHERE id=?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getId());
            statement.execute();
        }
    }

    // Delete a user
    public static void deleteUser(User user) throws SQLException, ClassNotFoundException {
        final String query = "DELETE FROM user WHERE id=?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, user.getId());
            statement.execute();
        }
    }

    // ... And so on for other methods ...
	
	// Utility method to execute queries that return a boolean result
    private static boolean executeUpdateReturningBoolean(PreparedStatement statement) throws SQLException {
        return statement.executeUpdate() > 0;
    }
	
    public static boolean isFollowing(int currentUserID, int potentialFollowingUserID) throws SQLException, ClassNotFoundException {
        final String sql = "SELECT id FROM following WHERE followed_by_user_id = ? AND following_user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, currentUserID);
            ps.setInt(2, potentialFollowingUserID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
	
    public static boolean followUser(int currentUserID, int userToFollowID) throws SQLException, ClassNotFoundException {
        final String sql = "INSERT INTO following(followed_by_user_id, following_user_id) VALUES (?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, currentUserID);
            ps.setInt(2, userToFollowID);
            return executeUpdateReturningBoolean(ps);
        }
    }
    
    public static boolean unfollowUser(int currentUserID, int userToUnfollowID) throws SQLException, ClassNotFoundException {
        final String sql = "DELETE FROM following WHERE followed_by_user_id=? AND following_user_id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, currentUserID);
            ps.setInt(2, userToUnfollowID);
            return executeUpdateReturningBoolean(ps);
        }
    }
	
	public static User getById(int userId) throws SQLException, ClassNotFoundException {
        final String query = "SELECT id, username, password, filename FROM user WHERE id=?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    return extractUserFromResultSet(results);
                }
            }
        }
        return null;
    }
	
	public static boolean hasUserLiked(int userId, int tweetId) throws SQLException, ClassNotFoundException {
        final String query = "SELECT * FROM likes WHERE user_id = ? AND tweet_id = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            try (ResultSet results = statement.executeQuery()) {
                return results.next();
            }
        }
    }

	public static boolean likeTweet(int userId, int tweetId) throws SQLException, ClassNotFoundException {
        final String query = "INSERT INTO likes (user_id, tweet_id) VALUES (?, ?)";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            return executeUpdateReturningBoolean(statement);
        }
    }
	
	public static boolean unlikeTweet(int userId, int tweetId) throws SQLException, ClassNotFoundException {
        final String query = "DELETE FROM likes WHERE user_id = ? AND tweet_id = ?";
        
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);
            return executeUpdateReturningBoolean(statement);
        }
    }

}
