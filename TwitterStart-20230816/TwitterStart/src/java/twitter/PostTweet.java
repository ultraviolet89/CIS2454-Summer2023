package twitter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PostTweet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String tweetText = request.getParameter("tweetContent");

        // Assuming user_id is stored in the session after user login. Adjust as necessary.
        int user_id = (int) request.getSession().getAttribute("user_id");

        // Create a new Tweet object
        Tweet newTweet = new Tweet(0, tweetText, new Timestamp(System.currentTimeMillis()), user_id);

        // Store the tweet in the database
        boolean success = storeTweet(newTweet);
        
        if (success) {
            // Successfully saved the tweet
            response.sendRedirect("index.jsp");
        } else {
            // Failed to save the tweet
            response.sendRedirect("error.jsp");
        }
    }

    private boolean storeTweet(Tweet tweet) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();

            // Assuming your database will auto-increment the tweet ID. Adjust as necessary.
            String sql = "INSERT INTO tweet (text, timestamp, user_id) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, tweet.getText());
            ps.setTimestamp(2, tweet.getTimestamp());
            ps.setInt(3, tweet.getUser_id());

            int result = ps.executeUpdate();

            return result > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
