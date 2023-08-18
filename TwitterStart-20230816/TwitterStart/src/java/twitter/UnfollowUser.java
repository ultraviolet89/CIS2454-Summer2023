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

@WebServlet(name = "UnfollowUser", urlPatterns = {"/UnfollowUser"})
public class UnfollowUser extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FollowUser.class.getName());
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int userToUnfollowID = Integer.parseInt(request.getParameter("unfollowUserId"));
            int currentUserID = (int) request.getSession().getAttribute("user_id");
            
            if (UserModel.unfollowUser(currentUserID, userToUnfollowID)) {
                response.sendRedirect("index.jsp?username=" + UserModel.getById(userToUnfollowID).getUsername());
            } else {
                response.sendRedirect("error.jsp");
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error while trying to follow user", e);
            response.sendRedirect("error.jsp");
        }
    }
}
