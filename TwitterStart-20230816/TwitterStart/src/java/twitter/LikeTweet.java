package twitter;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LikeTweet", urlPatterns = {"/LikeTweet"})
public class LikeTweet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        int currentUserId = (int) request.getSession().getAttribute("user_id");
        int tweetId = Integer.parseInt(request.getParameter("tweetId"));

        try {
            UserModel.likeTweet(currentUserId, tweetId);
            response.sendRedirect("index.jsp");
        } catch (SQLException | ClassNotFoundException ex) {
            // Logging the error and redirecting to the error page
            Logger.getLogger(LikeTweet.class.getName()).log(Level.SEVERE, "Error while liking tweet.", ex);
            response.sendRedirect("error.jsp");
        }
    }
}
