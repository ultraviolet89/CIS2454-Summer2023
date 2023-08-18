package twitter;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet(name = "FollowUser", urlPatterns = {"/FollowUser"})
public class FollowUser extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FollowUser.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userToFollowID = Integer.parseInt(request.getParameter("followUserId"));
            int currentUserID = (int) request.getSession().getAttribute("user_id");
            
            if (UserModel.followUser(currentUserID, userToFollowID)) response.sendRedirect("index.jsp?username=" + UserModel.getById(userToFollowID).getUsername());
            else response.sendRedirect("error.jsp");
            
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error while trying to follow user", e);
            // Redirect to error page
            response.sendRedirect("error.jsp");
        }
    }
}
