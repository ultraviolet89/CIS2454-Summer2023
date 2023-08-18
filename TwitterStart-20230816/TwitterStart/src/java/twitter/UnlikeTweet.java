package twitter;

import java.io.IOException;
import java.sql.SQLException; // Make sure to import SQLException
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet(name = "UnlikeTweet", urlPatterns = {"/UnlikeTweet"})
public class UnlikeTweet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int currentUserId = (int) request.getSession().getAttribute("user_id");
        int tweetId = Integer.parseInt(request.getParameter("tweetId"));
        try {
            UserModel.unlikeTweet(currentUserId, tweetId);
        } catch (ClassNotFoundException | SQLException ex) { // Catch both exceptions
            Logger.getLogger(UnlikeTweet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "An error occurred while unliking the tweet.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("index.jsp");
    }
}
