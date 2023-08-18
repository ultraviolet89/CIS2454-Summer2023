package twitter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Tweets {

    private static final String ALL_TWEETS_SQL = """
        SELECT 
            t1.id as id, 
            t1.text as text, 
            t1.timestamp as timestamp, 
            t1.user_id as user_id, 
            t2.username as username, 
            t2.filename as filename, 
            t2.image as image, 
            COUNT(t3.tweet_id) as likes_count
        FROM tweet as t1 
            LEFT JOIN user as t2 ON t2.id=t1.user_id 
            LEFT JOIN likes as t3 ON t1.id=t3.tweet_id
        GROUP BY t1.id
        ORDER BY t1.timestamp DESC;
    """;

    private static final String TWEETS_BY_USERNAME_SQL = """
        SELECT 
            t1.id as id, 
            t1.text as text, 
            t1.timestamp as timestamp, 
            t1.user_id as user_id, 
            t2.username as username, 
            t2.image as image, 
            t2.filename as filename, 
            COUNT(t3.tweet_id) as likes_count     
        FROM tweet as t1
            LEFT JOIN user as t2 ON t2.id = t1.user_id
            LEFT JOIN likes as t3 ON t1.id = t3.tweet_id
        WHERE t2.username = ?
        ORDER BY t1.timestamp DESC;
    """;
	
	private static final String TWEETS_FOR_USER_AND_FOLLOWING_SQL = """
        SELECT
            t1.id,
            t1.text,
            t1.user_id,
            t1.timestamp,
            t2.username as username,
            t2.image as image,
            t2.filename as filename,
            (SELECT COUNT(t3.id) FROM likes as t3 WHERE t3.tweet_id = t1.id) as likes_count
        FROM
        (
            -- Fetch tweets from the user
            SELECT
                t1.id,
                t1.text,
                t1.user_id,
                t1.timestamp
            FROM user
            JOIN tweet AS t1 ON user.id = t1.user_id
            WHERE user.username = ?

            UNION

            -- Fetch tweets from users they follow
            SELECT
                t3.id,
                t3.text,
                t3.user_id,
                t3.timestamp
            FROM user
            JOIN following AS t2 ON user.id = t2.followed_by_user_id
            JOIN tweet AS t3 ON t2.following_user_id = t3.user_id
            WHERE user.username = ?

            UNION

            -- Fetch tweets they've liked
            SELECT
                t5.id,
                t5.text,
                t5.user_id,
                t5.timestamp
            FROM user
            JOIN likes AS t4 ON user.id = t4.user_id
            JOIN tweet AS t5 ON t4.tweet_id = t5.id
            WHERE user.username = ?
        ) AS t1

        LEFT JOIN user as t2
            ON t2.id = t1.user_id

        ORDER BY t1.timestamp DESC;
    """;

    public static List<Tweet> getAllTweets() {
        List<Tweet> tweets = new ArrayList<>();
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(ALL_TWEETS_SQL);
            ResultSet rs = ps.executeQuery();
        ){ while (rs.next()) { tweets.add(extractTweetFromResultSet(rs)); } } 
        catch (SQLException | ClassNotFoundException e) { handleException(e); }
        return tweets;
    }

    public static List<Tweet> getTweetsByUsername(String username) {
        List<Tweet> tweets = new ArrayList<>();
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(TWEETS_BY_USERNAME_SQL);
        ){
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) { tweets.add(extractTweetFromResultSet(rs)); }
            }
        } 
        catch (SQLException | ClassNotFoundException e) { handleException(e); }
        return tweets;
    }

   public static List<Tweet> getTweetsForUserAndFollowing(String username) {
        List<Tweet> tweets = new ArrayList<>();
        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(TWEETS_FOR_USER_AND_FOLLOWING_SQL);
        ){
            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) { tweets.add(extractTweetFromResultSet(rs)); }
            }
        } 
        catch (SQLException | ClassNotFoundException e) { handleException(e); }
        return tweets;
    }

    private static Tweet extractTweetFromResultSet(ResultSet rs) throws SQLException {
        Tweet tweet = new Tweet(
            rs.getInt("id"),
            rs.getString("text"),
            rs.getTimestamp("timestamp"),
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("filename"),
            rs.getBytes("image")
        );
        tweet.setLikesCount(rs.getInt("likes_count"));
        return tweet;
    }

    private static void handleException(Exception e) {
        e.printStackTrace();
    }
}
